package com.examples.caseupdater.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Response {

  @JsonIgnore
  public Integer referenceId;


  public Integer getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(final Integer referenceId) {
    this.referenceId = referenceId;
  }
}
