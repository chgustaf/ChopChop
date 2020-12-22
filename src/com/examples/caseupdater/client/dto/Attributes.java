package com.examples.caseupdater.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Attributes {

  public String type;
  public String referenceId;

  @Override
  public String toString() {
    return "Attributes{" +
           "type='" + type + '\'' +
           ", referenceId='" + referenceId + '\'' +
           '}';
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public String id;

  public Attributes(final String type, final String referenceId) {
    this.type = type;
    this.referenceId = referenceId;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(final String referenceId) {
    this.referenceId = referenceId;
  }
}
