package com.examples.caseupdater.client.composite.batch;

import com.examples.caseupdater.client.composite.Result;

public class CompositeBatchResponse {



  boolean hasErrors;
  Result[] results;

  public boolean getHasErrors() {
    return hasErrors;
  }

  public void setHasErrors(final Boolean hasErrors) {
    this.hasErrors = hasErrors;
  }

  public Result[] getResults() {
    return results;
  }

  public void setResults(final Result[] results) {
    this.results = results;
  }

  private boolean isHasErrors() {
    return hasErrors;
  }

  private void setHasErrors(final boolean hasErrors) {
    this.hasErrors = hasErrors;
  }
}
