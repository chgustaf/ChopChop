package com.examples.caseupdater.client;

import com.examples.caseupdater.client.composite.InnerResult;
import com.examples.caseupdater.client.composite.batch.BatchRequest;
import com.examples.caseupdater.client.composite.batch.BatchRequestBuilder;
import com.examples.caseupdater.client.composite.batch.CombinedRequestResponse;
import com.examples.caseupdater.client.composite.batch.CompositeBatchRequest;
import com.examples.caseupdater.client.composite.batch.CompositeBatchRequestBuilder;
import com.examples.caseupdater.client.composite.batch.CompositeBatchResponse;
import com.examples.caseupdater.client.domain.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.ImprovedSalesforceClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompositeBatchTransaction {

  List<BatchRequest> requests;
  List<Record> records;
  CompositeBatchResponse compositeBatchResponse;
  List<CombinedRequestResponse> resultList;

  ObjectMapper mapper;
  boolean haltOnError;
  ImprovedSalesforceClient improvedSalesforceClient;


  private String baseURL = "v50.0/sobjects/";

  public CompositeBatchTransaction(
      final ImprovedSalesforceClient improvedSalesforceClient) {
    this(improvedSalesforceClient, false);
  }

  private CompositeBatchTransaction(final ImprovedSalesforceClient improvedSalesforceClient,
                                    final boolean haltOnError) {
    this.haltOnError = haltOnError;
    this.improvedSalesforceClient = improvedSalesforceClient;
    requests = new ArrayList<>();
    records = new ArrayList<>();
    resultList = new ArrayList<>();
  }


  public void update(Record record) throws JsonProcessingException {
    records.add(record);
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("PATCH")
            .setUrl(baseURL+record.getSObjectName()+"/"+record.getId())
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .createBatchRequest();
    requests.add(batchRequest);
  }

  public void create(Record record) throws JsonProcessingException {
    records.add(record);
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("POST")
            .setUrl(baseURL+record.getSObjectName())
            .setType(BatchRequest.Type.SOBJECT)
            .setSobjectName(record.getSObjectName())
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .setReferenceId(record.getReferenceId())
            .createBatchRequest();
    requests.add(batchRequest);
  }

  public void delete(Record record) throws JsonProcessingException {
    records.add(record);
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("DELETE")
            .setUrl(baseURL+record.getSObjectName()+"/"+record.getId())
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .setReferenceId(record.getReferenceId())
            .createBatchRequest();
    requests.add(batchRequest);
  }

  public void get(Record record) {
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("GET")
            .setUrl(baseURL+record.getSObjectName()+"/"+record.getId())
            .createBatchRequest();
    requests.add(batchRequest);
    //TODO: put the requested fields to the url
  }

  public boolean execute()
      throws IOException, AuthenticationException {
    String payload = renderPayload();
    String responseString = improvedSalesforceClient.compositeBatchCall(payload);

    compositeBatchResponse = parseCompositeBatchResponse(responseString);

    // I the number results differ from the number of batches then something is really wrong
    if (compositeBatchResponse.getResults().length != records.size()) {
      throw new RuntimeException("Something is meeeeessed up");
    }

    resultList = new ArrayList<>();
    for (int i = 0; i < compositeBatchResponse.getResults().length;i++) {

      if (requests.get(i).getMethod() == "DELETE") {

        resultList.add(
            new CombinedRequestResponse(
                requests.get(i),
                null,
                compositeBatchResponse.getResults()[i].getStatusCode()));
      } else {
        for (int j = 0; j < compositeBatchResponse.getResults()[i].getResult().length; j++) {
          resultList.add(
              new CombinedRequestResponse(
                  requests.get(i),
                  compositeBatchResponse.getResults()[i].getResult()[0],
                  compositeBatchResponse.getResults()[i].getStatusCode()));
        }
      }
    }

    if (compositeBatchResponse.getHasErrors()) {
      return false;
    }

    return true;
  }

  public CompositeBatchResponse parseCompositeBatchResponse(String stringResponse)
      throws JsonProcessingException {
    System.out.println(stringResponse);
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    return mapper.readValue(stringResponse, CompositeBatchResponse.class);

  }


  public String renderPayload() throws JsonProcessingException {
    BatchRequest[] requestsArray =
        requests.toArray(new BatchRequest[requests.size()]);
    CompositeBatchRequest request =
        new CompositeBatchRequestBuilder()
            .setBatchRequests(requestsArray)
            .setHaltOnError(false)
            .createCompositeBatchRequest();
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(request);
  }

  public List<Record> getResults() {
    return this.records;
  }

  public <T extends Record> T getRecord(String referenceId, Class<T> clazz)
      throws JsonProcessingException {

    // Find the request with the specific referenceId
    Optional<CombinedRequestResponse> result =
        resultList
            .stream()
            .filter(res -> res.getRequest() != null)
            .filter(res -> res.getRequest().getReferenceId().equals(referenceId))
            .findFirst();

    if (result.isPresent()) {
      ObjectMapper mapper = new ObjectMapper();

      String json;
      T record;
      boolean success;
      switch (result.get().getRequest().getMethod()) {
        case "POST" :
          // Merge the json that was sent with the id that was returned and create a new record
          // of type T
          success = (result.get().getStatusCode() == 201);
          json = result.get().getRequest().getRichInput();
          InnerResult innerResult = result.get().getResult();
          record = mapper.readValue(json, clazz);
          record.setId(innerResult.getId());
          record.setSuccess(success);
          record.setReferenceId(UUID.randomUUID().toString());
          return record;
        case "GET" :
          return null;
        case "PATCH" :
          return null;
        case "DELETE" :
          success = (result.get().getStatusCode() == 204);
          json = result.get().getRequest().getRichInput();
          record = mapper.readValue(json, clazz);
          record.setReferenceId(UUID.randomUUID().toString());
          record.setSuccess(success);
          return record;
        default :
          return null;
      }
    }
    return null;
  }
}
