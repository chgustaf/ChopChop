package com.examples.caseupdater.client.composite;

public class Result {
  int statusCode;
  InnerResult[] result;

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final int statusCode) {
    this.statusCode = statusCode;
  }

  public InnerResult[] getResult() {
    return result;
  }

  public void setResult(final InnerResult[] result) {
    this.result = result;
  }
}
