package com.salesforce.rest;

public class ResponseBuilder {

  private String id;
  private String type;
  private Response.Action action;
  private boolean success;
  private String statusCode;
  private String error;

  public ResponseBuilder setId(final String id) {
    this.id = id;
    return this;
  }

  public ResponseBuilder setType(final String type) {
    this.type = type;
    return this;
  }

  public ResponseBuilder setAction(final Response.Action action) {
    this.action = action;
    return this;
  }

  public ResponseBuilder setSuccess(final boolean success) {
    this.success = success;
    return this;
  }

  public ResponseBuilder setStatusCode(final String statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public ResponseBuilder setError(final String error) {
    this.error = error;
    return this;
  }

  public Response createResponse() {
    return new Response(id, action, success, statusCode, error);
  }
}