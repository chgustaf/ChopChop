package com.examples.caseupdater.client.composite;

public class InnerResult {
  String id;
  boolean success;
  String[] errors;
  String errorCode;
  String message;

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(final String errorCode) {
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(final boolean success) {
    this.success = success;
  }

  public String[] getErrors() {
    return errors;
  }

  public void setErrors(final String[] errors) {
    this.errors = errors;
  }
}
