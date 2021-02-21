package com.chgustaf.salesforce.client.composite.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class CombinedRequestResponse {

  public BatchRequest request;
  public JsonNode result;
  public Integer statusCode;


  public CombinedRequestResponse(
      final BatchRequest request, final JsonNode result, final Integer statusCode) {
    this.request = request;
    this.result = result;
    this.statusCode = statusCode;
  }

  public BatchRequest getRequest() {
    return request;
  }

  public void setRequest(final BatchRequest request) {
    this.request = request;
  }

  public JsonNode getResult() {
    return result;
  }

  public void setResult(final JsonNode result) {
    this.result = result;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final Integer statusCode) {
    this.statusCode = statusCode;
  }

  public boolean success() {
    if (statusCode != null) {
      return ((199 < statusCode) && (statusCode < 299));
    }
    return false;
  }
}
