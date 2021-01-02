package com.examples.caseupdater.client.composite;

public class RequestBuilder {

  private boolean allOrNone;
  private CompositeRequest[] compositeRequest;

  public RequestBuilder setAllOrNone(final boolean allOrNone) {
    this.allOrNone = allOrNone;
    return this;
  }

  public RequestBuilder setCompositeRequest(final CompositeRequest[] compositeRequests) {
    this.compositeRequest = compositeRequests;
    return this;
  }

  public Request createRequest() {
    return new Request(allOrNone, compositeRequest);
  }
}