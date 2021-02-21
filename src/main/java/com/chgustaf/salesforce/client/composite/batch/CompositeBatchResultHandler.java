package com.chgustaf.salesforce.client.composite.batch;

import com.chgustaf.salesforce.client.composite.domain.Record;
import com.chgustaf.salesforce.client.composite.domain.TransactionError;
import com.chgustaf.salesforce.client.composite.dto.BatchRequest;
import com.chgustaf.salesforce.client.composite.dto.CombinedRequestResponse;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompositeBatchResultHandler {

  private List<CombinedRequestResponse> results;
  private List<BatchRequest> requests;

  CompositeBatchResultHandler() {
    results = new ArrayList<>();
    requests = new ArrayList<>();
  }

  CompositeBatchResultHandler(List<BatchRequest> batchRequests,
                              CompositeBatchResponse compositeBatchResponse) {
    this();
    addResults(batchRequests, compositeBatchResponse);
  }

  void addResults(List<BatchRequest> batchRequests,
                  CompositeBatchResponse compositeBatchResponse) {
    this.requests.addAll(batchRequests);
    this.results.addAll(createResults(compositeBatchResponse));
  }

  List<CombinedRequestResponse> createResults(CompositeBatchResponse compositeBatchResponse) {
    List<CombinedRequestResponse> returnResultList = new ArrayList<>();
    for (int i = 0; i < compositeBatchResponse.getResults().length;i++) {
      createCombinedRequestResponse(requests.get(i), compositeBatchResponse, i)
          .ifPresent(returnResultList::add);
    }
    return returnResultList;
  }

  private Optional<CombinedRequestResponse> createCombinedRequestResponse(final BatchRequest theRequest,
                                                                          final CompositeBatchResponse theResponse,
                                                                          final int i) {
    Optional<CombinedRequestResponse> combinedRequestResponse;
    if (theRequest.getMethod().equals("DELETE") || theRequest.getMethod().equals("PATCH")) {
      combinedRequestResponse =
          Optional.of(
              new CombinedRequestResponse(
                  theRequest, null, theResponse.getResults()[i].getStatusCode()));
    } else {
      combinedRequestResponse = Optional.of(new CombinedRequestResponse(
          theRequest,
          theResponse.getResults()[i].getResult(),
          theResponse.getResults()[i].getStatusCode()));
    }
    return combinedRequestResponse;
  }

  <T extends Record> T getRecord(String referenceId, Class<T> clazz)
      throws IOException {

    // Find the request with the specific referenceId
    Optional<CombinedRequestResponse> result = findRequestResponse(referenceId);

    if (result.isPresent()) {
      CombinedRequestResponse theResult = result.get();
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
      mapper.registerModule(new JavaTimeModule());

      String json;
      T record;
      boolean success;
      switch (result.get().getRequest().getMethod()) {
        case "POST" :
          // Merge the json that was sent with the id that was returned and create a new record
          // of type T
          success = (theResult.getStatusCode() == 201);
          json = theResult.getRequest().getRichInput();
          JsonNode innerResult = theResult.getResult();
          record = mapper.readValue(json, clazz);
          if (success) {
            record.setId(innerResult.get("id").textValue());
          } else {
            ObjectReader reader = mapper.readerFor(new TypeReference<List<TransactionError>>() {});
            List<TransactionError> errorList = reader.readValue(innerResult);
            record.setErrors(errorList);
          }
          record.setSuccess(success);
          record.setReferenceId(UUID.randomUUID().toString());
          return record;
        case "GET" :
          return retrieveGetRecord(theResult, mapper, clazz);
        case "PATCH" :
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

  private <T extends Record> T retrieveGetRecord(CombinedRequestResponse result,
                                                 ObjectMapper mapper, Class<T> clazz)
      throws IOException {
    boolean success = (result.getStatusCode() == 200);
    String json = result.getRequest().getRichInput();
    JsonNode innerResult = result.getResult();
    T record = mapper.readValue(json, clazz);
    if (success) {
      record = mapper.treeToValue(innerResult, clazz);
    } else {
      ObjectReader reader = mapper.readerFor(new TypeReference<List<TransactionError>>() {});
      List<TransactionError> errorList = reader.readValue(innerResult);
      record.setErrors(errorList);
    }
    record.setReferenceId(UUID.randomUUID().toString());
    record.setId(result.getRequest().getId());
    record.setSuccess(success);
    return record;
  }

  Optional<CombinedRequestResponse> findRequestResponse(String referenceId) {
    return results.stream()
        .filter(res -> res.getRequest() != null)
        .filter(res -> res.getRequest().getReferenceId().equals(referenceId))
        .findFirst();
  }
}

