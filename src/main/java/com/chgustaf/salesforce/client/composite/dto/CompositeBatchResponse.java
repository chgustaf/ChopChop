package com.chgustaf.salesforce.client.composite.dto;

public class CompositeBatchResponse {

  boolean hasErrors;
  Result[] results;

  public boolean getHasErrors() {
    return hasErrors;
  }

  public Result[] getResults() {
    return results;
  }

  public void setResults(final Result[] results) {
    this.results = results;
  }

  public void setHasErrors(final boolean hasErrors) {
    this.hasErrors = hasErrors;
  }
}
