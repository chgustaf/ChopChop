package com.examples.caseupdater.client.domain;

public class Query {

  public String query;
  public String referenceId;

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
}
