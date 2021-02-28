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

      combinedRequestResponse = Optional.of(new CombinedRequestResponse(
          theRequest,
          theResponse.getResults()[i].getResult(),
          theResponse.getResults()[i].getStatusCode()));

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

      switch (result.get().getRequest().getMethod()) {
        case "POST" :
          return retrievePostRecord(theResult, mapper, clazz);
        case "GET" :
          return retrieveGetRecord(theResult, mapper, clazz);
        case "PATCH" :
        case "DELETE" :
          return retrievePatchDeleteRecord(theResult, mapper, clazz);
        default :
          return null;
      }
    }
    return null;
  }

  private <T extends  Record> T retrievePostRecord(CombinedRequestResponse result,
                                                   ObjectMapper mapper, Class<T> clazz)
      throws IOException {
    boolean success = (result.getStatusCode() > 199 && result.getStatusCode() < 300);
    String json = result.getRequest().getRichInput();
    JsonNode innerResult = result.getResult();
    T record = mapper.readValue(json, clazz);
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
  }

  private <T extends Record> T retrieveGetRecord(CombinedRequestResponse result,
                                                 ObjectMapper mapper, Class<T> clazz)
      throws IOException {
    boolean success = (result.getStatusCode() > 199 && result.getStatusCode() < 300);
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

  private <T extends Record> T retrievePatchDeleteRecord(CombinedRequestResponse result,
                                                         ObjectMapper mapper, Class<T> clazz)
      throws IOException {
    boolean success = (result.getStatusCode() > 199 && result.getStatusCode() < 300);
    String json = result.getRequest().getRichInput();
    T record = mapper.readValue(json, clazz);
    JsonNode innerResult = result.getResult();
    if (!success) {
      ObjectReader reader = mapper.readerFor(new TypeReference<List<TransactionError>>() {});
      List<TransactionError> errorList = reader.readValue(innerResult);
      record.setErrors(errorList);
    }
    record.setReferenceId(UUID.randomUUID().toString());
    record.setId(result.getRequest().getId());
    record.setSuccess(success);
    return record;
  }

  Optional<CombinedRequestResponse>  findRequestResponse(String referenceId) {
    return results.stream()
        .filter(res -> res.getRequest() != null)
        .filter(res -> res.getRequest().getReferenceId().equals(referenceId))
        .findFirst();
  }
}

