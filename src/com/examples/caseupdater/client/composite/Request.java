package com.examples.caseupdater.client.composite;

public class Request {

  boolean allOrNone;
  CompositeRequest[] compositeRequest;

  Request(final boolean allOrNone,
          final CompositeRequest[] compositeRequest) {
    this.allOrNone = allOrNone;
    this.compositeRequest = compositeRequest;
  }

  public boolean isAllOrNone() {
    return allOrNone;
  }

  public void setAllOrNone(final boolean allOrNone) {
    this.allOrNone = allOrNone;
  }

  public CompositeRequest[] getCompositeRequest() {
    return compositeRequest;
  }

  public void setCompositeRequest(
      final CompositeRequest[] compositeRequest) {
    this.compositeRequest = compositeRequest;
  }
}
