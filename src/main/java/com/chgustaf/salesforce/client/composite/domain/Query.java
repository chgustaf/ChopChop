package com.chgustaf.salesforce.client.composite.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

public class Query<T> {

  public String query;
  public String referenceId;
  public Class<T> sobject;

  public Query(Class<T> clazz) {
    sobject = clazz;
    this.referenceId = UUID.randomUUID().toString();
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(final String query) {
    this.query = query;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(final String referenceId) {
    this.referenceId = referenceId;
  }

  public String getSobjectName() {
    return sobject.getClass().getName();
  }

  @JsonIgnore
  public Class<T> getEntityClass() {
    return sobject;
  }
}
