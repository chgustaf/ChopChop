package com.chgustaf.salesforce.client.composite.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

public class Query<T> {

  String query;
  // Used for nextUrl when number of records are too many to return in one go
  String url;
  String referenceId;
  Class<T> sobject;

  public Query(final Class<T> sobject) {
    this.sobject = sobject;
    this.referenceId = UUID.randomUUID().toString();
  }

  public Query(String query, Class<T> clazz) {
    this(clazz);
    this.query = query;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  @JsonIgnore
  public Class<T> getEntityClass() {
    return sobject;
  }
}
