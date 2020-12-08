package com.salesforce.rest;

public class Response {

  enum Action {GET, POST, PATCH, DELETE}

  String id;
  Action action;
  boolean success;
  String statusCode;
  String error;

  public Response(final String id, final Action action, final boolean success,
                   final String statusCode, final String error) {
    this.id = id;
    this.action = action;
    this.success = success;
    this.statusCode = statusCode;
    this.error = error;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public Action getAction() {
    return action;
  }

  public void setAction(final Action action) {
    this.action = action;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(final boolean success) {
    this.success = success;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final String statusCode) {
    this.statusCode = statusCode;
  }

  public String getError() {
    return error;
  }

  public void setError(final String error) {
    this.error = error;
  }
}
