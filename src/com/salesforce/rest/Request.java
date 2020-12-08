package com.salesforce.rest;

public class Request {

  public String type;

  public String endpoint;

  public String body;


  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(final String endpoint) {
    this.endpoint = endpoint;
  }

  public String getBody() {
    return body;
  }

  public void setBody(final String body) {
    this.body = body;
  }
}
