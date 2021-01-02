package com.examples.caseupdater.client.composite;

public class CompositeRequestBuilder {

  private String method;
  private String url;
  private String body;

  public CompositeRequestBuilder setMethod(final String method) {
    this.method = method;
    return this;
  }

  public CompositeRequestBuilder setUrl(final String url) {
    this.url = url;
    return this;
  }

  public CompositeRequestBuilder setBody(final String body) {
    this.body = body;
    return this;
  }

  public CompositeRequest createCompositeRequest() {
    return new CompositeRequest(method, url, body);
  }
}