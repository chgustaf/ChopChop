package com.salesforce.rest;

public class TransactionResponseBuilder {

  private boolean success;
  private String error;
  private String failureReason;
  private String endpoint;
  private String responseBody;
  private String requestBody;
  private int statusCode;
  private TransactionResponse.Action method;

  public TransactionResponseBuilder setSuccess(final boolean success) {
    this.success = success;
    return this;
  }

  public TransactionResponseBuilder setError(final String error) {
    this.error = error;
    return this;
  }

  public TransactionResponseBuilder setFailureReason(final String failureReason) {
    this.failureReason = failureReason;
    return this;
  }

  public TransactionResponseBuilder setEndpoint(final String endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  public TransactionResponseBuilder setResponseBody(final String responseBody) {
    this.responseBody = responseBody;
    return this;
  }

  public TransactionResponseBuilder setRequestBody(final String requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  public TransactionResponseBuilder setStatusCode(final int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public TransactionResponseBuilder setMethod(final TransactionResponse.Action method) {
    this.method = method;
    return this;
  }

  public TransactionResponse createTransactionResponse() {
    return new TransactionResponse(success, error, failureReason, endpoint, responseBody,
        requestBody, statusCode, method);
  }
}