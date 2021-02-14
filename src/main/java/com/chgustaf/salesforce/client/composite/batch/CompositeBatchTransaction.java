package com.chgustaf.salesforce.client.composite.batch;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Query;
import com.chgustaf.salesforce.client.composite.domain.Record;
import com.chgustaf.salesforce.client.composite.dto.BatchRequest;
import com.chgustaf.salesforce.client.composite.dto.BatchRequestBuilder;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchRequest;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchRequestBuilder;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompositeBatchTransaction {

  private final String SOBJECT_URL = "v50.0/sobjects/";
  private final String QUERY_URL = "v50.0/query/";

  private boolean haltOnError;

  private List<BatchRequest> requests;
  private boolean alreadyExecuted;

  SalesforceCompositeBatchClient salesforceCompositeBatchClient;
  CompositeBatchResultHandler resultHandler;

  CompositeBatchTransaction(SalesforceCompositeBatchClient salesforceCompositeBatchClient,
                            boolean haltOnError) {
    this(salesforceCompositeBatchClient, haltOnError, false);
  }

  CompositeBatchTransaction(
      final SalesforceCompositeBatchClient salesforceCompositeBatchClient,
      final boolean haltOnError, final boolean alreadyExecuted) {
    this.requests = new ArrayList<>();
    this.salesforceCompositeBatchClient = salesforceCompositeBatchClient;
    this.haltOnError = haltOnError;
    this.alreadyExecuted = alreadyExecuted;
  }

  void create(Record record) throws JsonProcessingException {
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("POST")
            .setUrl(SOBJECT_URL + record.getSObjectName())
            .setType(BatchRequest.Type.SOBJECT)
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .setReferenceId(record.getReferenceId())
            .createBatchRequest();
    requests.add(batchRequest);
  }

  void get(Record record) throws JsonProcessingException {
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("GET")
            .setUrl(
                SOBJECT_URL
                + record.getSObjectName()
                + "/"
                + record.getId()
                + "?fields="
                + String.join(",", record.getAllFields()))
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .setReferenceId(record.getReferenceId())
            .setType(BatchRequest.Type.SOBJECT)
            .setId(record.getId())
            .createBatchRequest();
    requests.add(batchRequest);
  }

  void get(String url, String referenceId) throws JsonProcessingException {
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("GET")
            .setUrl(url)
            .setReferenceId(referenceId)
            .setType(BatchRequest.Type.SOBJECT)
            .createBatchRequest();
    requests.add(batchRequest);
  }

  void update(Record record) throws JsonProcessingException {
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("PATCH")
            .setUrl(SOBJECT_URL + record.getSObjectName() + "/" + record.getId())
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .setReferenceId(record.getReferenceId())
            .setId(record.getId())
            .setType(BatchRequest.Type.SOBJECT)
            .createBatchRequest();
    requests.add(batchRequest);
  }

  void delete(Record record) throws JsonProcessingException {
    BatchRequest batchRequest =
        new BatchRequestBuilder()
            .setMethod("DELETE")
            .setUrl(SOBJECT_URL + record.getSObjectName() + "/" + record.getId())
            .setRichInput(record.getJSON().replaceAll("\\\\\"", "\""))
            .setReferenceId(record.getReferenceId())
            .setId(record.getId())
            .setType(BatchRequest.Type.SOBJECT)
            .createBatchRequest();
    requests.add(batchRequest);
  }

  void query(Query query) {
    BatchRequest batchRequest;
    if (query.getUrl()!= null) {
      batchRequest =
          new BatchRequestBuilder()
              .setMethod("GET")
              .setUrl(query.getUrl())
              .setType(BatchRequest.Type.QUERY)
              .setReferenceId(query.getReferenceId())
              .createBatchRequest();
    } else {
      batchRequest =
          new BatchRequestBuilder()
              .setMethod("GET")
              .setUrl(QUERY_URL + "?q=" + query.getQuery())
              .setType(BatchRequest.Type.QUERY)
              .setReferenceId(query.getReferenceId())
              .createBatchRequest();
    }
    requests.add(batchRequest);
  }

  boolean execute() throws IOException, AuthenticationException {
    if (alreadyExecuted) {
      return false;
    }
    String payload = renderPayload();
    String responseString = salesforceCompositeBatchClient.compositeBatchCall(payload);
    CompositeBatchResponse compositeBatchResponse = parseCompositeBatchResponse(responseString);
    resultHandler = new CompositeBatchResultHandler(requests, compositeBatchResponse);
    alreadyExecuted = true;
    if (compositeBatchResponse.getHasErrors()) {
      return false;
    }
    return true;
  }

  String renderPayload() throws JsonProcessingException {
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

  CompositeBatchResponse parseCompositeBatchResponse(String stringResponse)
      throws JsonProcessingException {
    System.out.println("String response "+ stringResponse);

    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    return mapper.readValue(stringResponse, CompositeBatchResponse.class);
  }

  public <T extends Record> T getRecord(String referenceId, Class<T> clazz) throws IOException {
    if (resultHandler == null) {
      return null;
    }
    return resultHandler.getRecord(referenceId, clazz);
  }

  public <T extends Record> List<T> getQueryResult(String referenceId, Class<T> clazz)
      throws IOException {
    if (resultHandler == null) {
      return null;
    }
    return resultHandler.getQueryResult(referenceId, clazz);
  }

  public String hasMore(String referenceId) throws JsonProcessingException {
    return resultHandler.hasMore(referenceId);
  }
}
