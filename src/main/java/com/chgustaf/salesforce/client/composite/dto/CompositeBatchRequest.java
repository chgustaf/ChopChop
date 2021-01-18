package com.chgustaf.salesforce.client.composite.dto;

public class CompositeBatchRequest {

  /*
  Limits—vXX.X/limits
  SObject main.resources—vXX.X/sobjects/ DONE!
  Query—vXX.X/query/?q=soql
  QueryAll—vXX.X/queryAll/?q=soql
  Search—vXX.X/search/?q=sosl
  Connect main.resources—vXX.X/connect/
  Chatter main.resources—vXX.X/chatter/
   */
  BatchRequest[] batchRequests;
  boolean haltOnError;

  CompositeBatchRequest(
      final BatchRequest[] batchRequests, final boolean haltOnError) {
    this.batchRequests = batchRequests;
    this.haltOnError = haltOnError;
  }

  public BatchRequest[] getBatchRequests() {
    return batchRequests;
  }

  public void setBatchRequests(final BatchRequest[] batchRequests) {
    this.batchRequests = batchRequests;
  }

  public boolean isHaltOnError() {
    return haltOnError;
  }

  public void setHaltOnError(final boolean haltOnError) {
    this.haltOnError = haltOnError;
  }
}
