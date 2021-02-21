package com.chgustaf.salesforce.client.composite.batch;

import com.chgustaf.salesforce.client.composite.domain.QueryResult;
import com.chgustaf.salesforce.client.composite.domain.Record;
import com.chgustaf.salesforce.client.composite.domain.TransactionError;
import com.chgustaf.salesforce.client.composite.dto.BatchRequest;
import com.chgustaf.salesforce.client.composite.dto.CombinedRequestResponse;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CompositeBatchQueryResultHandler {

  private List<BatchRequest> requests;
  private List<CombinedRequestResponse> results;

  public CompositeBatchQueryResultHandler() {
    this.requests = new ArrayList<>();
    this.results = new ArrayList<>();
  }

  void addResults(List<BatchRequest> batchRequests,
                  CompositeBatchResponse compositeBatchResponse) {
    this.requests.addAll(batchRequests);
    this.results.addAll(createResults(compositeBatchResponse));
  }

  <T extends Record> List<T> getQueryResultList(String referenceId, Class<T> clazz)
      throws IOException {
    QueryResult queryResult = getQueryResult(referenceId);
    ObjectMapper mapper = getObjectMapper();
    ObjectReader reader = mapper.readerForArrayOf(clazz);
    List<T> returnList = new ArrayList<>();
    T[] arrayofT = reader.readValue(queryResult.records);
    returnList.addAll(Arrays.asList(arrayofT));
    return returnList;
  }


  String nextRecordsUrl(String referenceId) throws IOException {
    QueryResult queryResult = getQueryResult(referenceId);
    if (queryResult != null) {
      return queryResult.nextRecordsUrl;
    }
    return null;
  }

  boolean done(String referenceId) throws IOException {
    QueryResult queryResult = getQueryResult(referenceId);
    if (queryResult != null) {
      return queryResult.done;
    }
    return false;
  }

  QueryResult getQueryResult(String referenceId) throws IOException {
    // Find the request with the specific referenceId
    Optional<CombinedRequestResponse> result = findRequestResponse(referenceId, results);
    if (result.isPresent()) {
      return parseQueryResult(result.get());
    }
    return null;
  }

  public QueryResult parseQueryResult(CombinedRequestResponse combinedRequestResponse)
      throws IOException {
    QueryResult queryResult;
    ObjectMapper mapper = getObjectMapper();
    if (combinedRequestResponse.success()) {
      queryResult = mapper.treeToValue(
          combinedRequestResponse.result,
          QueryResult.class);
      queryResult.success = true;
    } else {
      ObjectReader reader = mapper.readerFor(new TypeReference<List<TransactionError>>() {});
      List<TransactionError> errorList = reader.readValue(combinedRequestResponse.result);
      queryResult = new QueryResult();
      queryResult.errors = errorList;
      queryResult.success = false;
    }
    return queryResult;
  }

  ObjectMapper getObjectMapper() {
    ObjectMapper mapper = JsonMapper.builder()
        .addModule(new ParameterNamesModule())
        .addModule(new Jdk8Module())
        .addModule(new JavaTimeModule())
        .build();
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    return mapper;
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

  Optional<CombinedRequestResponse> findRequestResponse(String referenceId,
                                                        List<CombinedRequestResponse> results) {
    return results.stream()
        .filter(res -> res.getRequest() != null)
        .filter(res -> res.getRequest().getReferenceId().equals(referenceId))
        .findFirst();
  }
}
