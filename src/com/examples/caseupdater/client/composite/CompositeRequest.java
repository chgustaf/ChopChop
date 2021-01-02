package com.examples.caseupdater.client.composite;

import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.UUID;

public class  CompositeRequest {
  String method;
  String url;
  String referenceId;
  String body;

  public CompositeRequest(final String method, final String url, final String body) {
    this.method = method;
    this.url = url;
    //this.referenceId = referenceId;
    this.referenceId = UUID.randomUUID().toString();
    this.body = body;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(final String method) {
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(final String referenceId) {
    this.referenceId = referenceId;
  }

  @JsonRawValue
  public String getBody() {
    return body;
  }

  public void setBody(final String body) {
    this.body = body;
  }
}
