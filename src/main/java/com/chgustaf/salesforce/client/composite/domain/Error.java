package com.chgustaf.salesforce.client.composite.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Error {

  private String errorCode;
  private String message;

  @JsonIgnore
  public String getErrorCode() {
    return this.errorCode;
  }

  public void setErrorCode(final String errorCode) {
    this.errorCode = errorCode;
  }

  @JsonIgnore
  public String getMessage() {
    return this.message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
