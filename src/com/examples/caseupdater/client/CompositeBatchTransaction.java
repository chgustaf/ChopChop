package com.examples.caseupdater.client;

import com.examples.caseupdater.client.composite.batch.BatchRequest;
import com.examples.caseupdater.client.composite.batch.BatchRequestBuilder;
import com.examples.caseupdater.client.composite.batch.CombinedRequestResponse;
import com.examples.caseupdater.client.composite.batch.CompositeBatchRequest;
import com.examples.caseupdater.client.composite.batch.CompositeBatchRequestBuilder;
import com.examples.caseupdater.client.composite.batch.CompositeBatchResponse;
import com.examples.caseupdater.client.domain.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.ImprovedSalesforceClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompositeBatchTransaction {

  private List<BatchRequest> requests;
  private List<Record> records;
  private CompositeBatchResponse compositeBatchResponse;
  private List<CombinedRequestResponse> resultList;

  private boolean haltOnError;
  private ImprovedSalesforceClient improvedSalesforceClient;
  private Boolean alreadyExecuted = false;

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
            .setReferenceId(record.getReferenceId())
            .setId(record.getId())
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
            .setId(record.getId())
            .createBatchRequest();
    requests.add(batchRequest);
  }

  public void get(Record record) {
    records.add(record);
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("GET")
            .setUrl(baseURL+record.getSObjectName()+"/"+record.getId()+"?fields="+String.join(
                ",", record.getAllFields()))
            .setReferenceId(record.getReferenceId())
            .setId(record.getId())
            .createBatchRequest();
    requests.add(batchRequest);
  }

  public boolean execute()
      throws IOException, AuthenticationException {
    if (alreadyExecuted) {
      return false;
    }
    String payload = renderPayload();
    String responseString = improvedSalesforceClient.compositeBatchCall(payload);
    compositeBatchResponse = parseCompositeBatchResponse(responseString);

    // I the number results differ from the number of batches then something is really wrong
    if (compositeBatchResponse.getResults().length != records.size()) {
      throw new RuntimeException("Something is meeeeessed up");
    }

    resultList = new ArrayList<>();
    for (int i = 0; i < compositeBatchResponse.getResults().length;i++) {
      if (requests.get(i).getMethod() == "DELETE" || requests.get(i).getMethod() == "PATCH") {
        resultList.add(
            new CombinedRequestResponse(
                requests.get(i),
                null,
                compositeBatchResponse.getResults()[i].getStatusCode()));
      } else {
          resultList.add(
              new CombinedRequestResponse(
                  requests.get(i),
                  compositeBatchResponse.getResults()[i].getResult(),
                  compositeBatchResponse.getResults()[i].getStatusCode()));
      }
    }

    alreadyExecuted = true;
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
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    return mapper.readValue(stringResponse, CompositeBatchResponse.class);

  }


  public String renderPayload() throws JsonProcessingException {
    BatchRequest[] requestsArray =
        requests.toArray(new BatchRequest[requests.size()]);
    CompositeBatchRequest request =
        new CompositeBatchRequestBuilder()
            .setBatchRequests(requestsArray)
            .setHaltOnError(haltOnError)
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
      mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

      String json;
      T record;
      boolean success;
      switch (result.get().getRequest().getMethod()) {
        case "POST" :
          // Merge the json that was sent with the id that was returned and create a new record
          // of type T
          success = (result.get().getStatusCode() == 201);
          json = result.get().getRequest().getRichInput();
          JsonNode innerResult = result.get().getResult();
          record = mapper.readValue(json, clazz);
          if (success) {
            record.setId(innerResult.get("id").textValue());
          }
          record.setSuccess(success);
          record.setReferenceId(UUID.randomUUID().toString());
          return record;
        case "GET" :
          success = (result.get().getStatusCode() == 200);
          JsonNode jsonN =  result.get().getResult();
          record = mapper.treeToValue(jsonN, clazz);
          record.setReferenceId(UUID.randomUUID().toString());
          record.setId(result.get().getRequest().getId());
          record.setSuccess(success);
          return record;
        case "PATCH" :
          success = (result.get().getStatusCode() == 204);
          json = result.get().getRequest().getRichInput();
          record = mapper.readValue(json, clazz);
          record.setReferenceId(UUID.randomUUID().toString());
          record.setId(result.get().getRequest().getId());
          record.setSuccess(success);
          return record;
        case "DELETE" :
          success = (result.get().getStatusCode() == 204);
          json = result.get().getRequest().getRichInput();
          record = mapper.readValue(json, clazz);
          record.setReferenceId(UUID.randomUUID().toString());
          record.setId(result.get().getRequest().getId());
          record.setSuccess(success);
          return record;
        default :
          return null;
      }
    }
    return null;
  }
}
