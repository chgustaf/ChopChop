package com.salesforce.rest;

public class TransactionResponse {

  boolean success;
  String error;
  String failureReason;
  public enum Action {GET, POST, PATCH, DELETE}
  String endpoint;
  String responseBody;
  String requestBody;
  int statusCode;
  Action method;


  TransactionResponse(final boolean success, final String error, final String failureReason,
                      final String endpoint, final String responseBody,
                      final String requestBody,
                      final int statusCode, final Action method) {
    this.success = success;
    this.error = error;
    this.failureReason = failureReason;
    this.endpoint = endpoint;
    this.responseBody = responseBody;
    this.requestBody = requestBody;
    this.statusCode = statusCode;
    this.method = method;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public void setFailureReason(final String failureReason) {
    this.failureReason = failureReason;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(final String requestBody) {
    this.requestBody = requestBody;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(final String endpoint) {
    this.endpoint = endpoint;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(final String responseBody) {
    this.responseBody = responseBody;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final int statusCode) {
    this.statusCode = statusCode;
  }

  public Action getMethod() {
    return method;
  }

  public void setMethod(final Action method) {
    this.method = method;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(final boolean success) {
    this.success = success;
  }

  public String getError() {
    return error;
  }

  public void setError(final String error) {
    this.error = error;
  }
}
