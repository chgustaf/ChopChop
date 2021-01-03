package com.examples.caseupdater.client.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public class Case implements Record {

  public String id;
  public String name;
  public String accountId;


  @Override
  public Boolean getSuccess() {
    return null;
  }

  @Override
  public String getSObjectName() {
    return null;
  }

  @Override
  public String getReferenceId() {
    return null;
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public String getJSON() throws JsonProcessingException {
    return null;
  }

  @Override
  public Integer getStatusCode() {
    return null;
  }

  @Override
  public void setSuccess(final Boolean success) {

  }
  @Override
  public void setReferenceId(final String referenceId) {

  }

  @Override
  public void setStatusCode(final Integer statusCode) {

  }

  @Override
  public List<String> getAllFields() {
    return null;
  }

  public void setId(final String id) {

  }

  @Override
  public void setJSON(final String json) {

  }
}
