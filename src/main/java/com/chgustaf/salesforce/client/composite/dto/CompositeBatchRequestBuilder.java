package com.chgustaf.salesforce.client.composite.dto;

public class CompositeBatchRequestBuilder {

  private BatchRequest[] batchRequests;
  private boolean haltOnError;

  public CompositeBatchRequestBuilder setBatchRequests(final BatchRequest[] batchRequests) {
    this.batchRequests = batchRequests;
    return this;
  }

  public CompositeBatchRequestBuilder setHaltOnError(final boolean haltOnError) {
    this.haltOnError = haltOnError;
    return this;
  }

  public CompositeBatchRequest createCompositeBatchRequest() {
    return new CompositeBatchRequest(batchRequests, haltOnError);
  }
}