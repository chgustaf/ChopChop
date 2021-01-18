package com.chgustaf.salesforce.client.composite.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class Result {

  int statusCode;
  JsonNode result;

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final int statusCode) {
    this.statusCode = statusCode;
  }

  public JsonNode getResult() {
    return result;
  }

  public void setResult(final JsonNode result) {
    this.result = result;
  }
}
