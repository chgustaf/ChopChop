package com.chgustaf.salesforce.client.composite.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Query;
import com.chgustaf.salesforce.client.composite.domain.QueryResult;
import com.chgustaf.salesforce.client.composite.domain.Record;
import com.chgustaf.salesforce.client.composite.dto.BatchRequest;
import com.chgustaf.salesforce.client.composite.dto.BatchRequestBuilder;
import com.chgustaf.salesforce.client.composite.dto.CombinedRequestResponse;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchRequest;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchRequestBuilder;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompositeBatchTransaction {

  private List<BatchRequest> requests;
  private List<Record> records;
  private List<Query> queries;
  private CompositeBatchResponse compositeBatchResponse;
  private List<CombinedRequestResponse> resultList;

  private boolean haltOnError;
  private SalesforceCompositeBatchClient salesforceCompositeBatchClient;
  private Boolean alreadyExecuted = false;

  private String sobjectURL = "v50.0/sobjects/";
  private String queryURL = "v50.0/query/";

  public CompositeBatchTransaction(
      final SalesforceCompositeBatchClient salesforceCompositeBatchClient) {
    this(salesforceCompositeBatchClient, false);
  }

  private CompositeBatchTransaction(final SalesforceCompositeBatchClient salesforceCompositeBatchClient,
                                    final boolean haltOnError) {
    this.haltOnError = haltOnError;
    this.salesforceCompositeBatchClient = salesforceCompositeBatchClient;
    requests = new ArrayList<>();
    records = new ArrayList<>();
    resultList = new ArrayList<>();
    queries = new ArrayList<>();
  }

  public void query(Query query) {
    queries.add(query);
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("GET")
            .setUrl(queryURL + "?q=" + query.getQuery())
            .setType(BatchRequest.Type.QUERY)
            .setReferenceId(query.getReferenceId())
            .createBatchRequest();
    requests.add(batchRequest);
  }

  public void update(Record record) throws JsonProcessingException {
    records.add(record);
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("PATCH")
            .setUrl(sobjectURL + record.getSObjectName() + "/" + record.getId())
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .setReferenceId(record.getReferenceId())
            .setId(record.getId())
            .setType(BatchRequest.Type.SOBJECT)
            .createBatchRequest();
    requests.add(batchRequest);
  }

  public void create(Record record) throws JsonProcessingException {
    records.add(record);
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("POST")
            .setUrl(sobjectURL + record.getSObjectName())
            .setType(BatchRequest.Type.SOBJECT)
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
            .setUrl(sobjectURL + record.getSObjectName() + "/" + record.getId())
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .setReferenceId(record.getReferenceId())
            .setId(record.getId())
            .setType(BatchRequest.Type.SOBJECT)
            .createBatchRequest();
    requests.add(batchRequest);
  }

  public void get(Record record) {
    records.add(record);
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("GET")
            .setUrl(
                sobjectURL
                    + record.getSObjectName()
                    + "/"
                    + record.getId()
                    + "?fields="
                    + String.join(",", record.getAllFields()))
            .setReferenceId(record.getReferenceId())
            .setType(BatchRequest.Type.SOBJECT)
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
    String responseString = salesforceCompositeBatchClient.compositeBatchCall(payload);
    compositeBatchResponse = parseCompositeBatchResponse(responseString);

    // I the number results differ from the number of batches then something is really wrong
    if (compositeBatchResponse.getResults().length != (records.size()+queries.size())) {
      throw new RuntimeException("Something is meeeeessed up");
    }

    resultList = new ArrayList<>();
    for (int i = 0; i < compositeBatchResponse.getResults().length;i++) {

      // TODO fix so that the java application will call while done != true
      if (requests.get(i).getType().equals(BatchRequest.Type.SOBJECT)) {
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

      } else if (requests.get(i).getType().equals(BatchRequest.Type.QUERY)) {
        resultList.add(
            new CombinedRequestResponse(requests.get(i),
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

    public <T extends Record> List<T> getQueryResult(String referenceId, Class<T> clazz)
      throws IOException {
    // Find the request with the specific referenceId
    Optional<CombinedRequestResponse> result =
        resultList.stream()
            .filter(res -> res.getRequest() != null)
            .filter(res -> res.getRequest().getReferenceId().equals(referenceId))
            .findFirst();

    List<T> returnList = new ArrayList<>();
    if (result.isPresent()) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
      JsonNode jsonNode = result.get().getResult();
      QueryResult queryResult = mapper.treeToValue(jsonNode, QueryResult.class);

      ObjectReader reader = mapper.readerForArrayOf(clazz);
      T[] arrayofT = reader.readValue(queryResult.records);
      returnList.addAll(Arrays.asList(arrayofT));
    }
    return returnList;
  }
}
