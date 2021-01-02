package com.examples.caseupdater.client.composite.batch;

public class CompositeBatchRequest {

  /*
  Limits—vXX.X/limits
  SObject resources—vXX.X/sobjects/
  Query—vXX.X/query/?q=soql
  QueryAll—vXX.X/queryAll/?q=soql
  Search—vXX.X/search/?q=sosl
  Connect resources—vXX.X/connect/
  Chatter resources—vXX.X/chatter/
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
