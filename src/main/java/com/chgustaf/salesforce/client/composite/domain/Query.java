package com.chgustaf.salesforce.client.composite.domain;

import java.util.UUID;

public class Query {

  public String query;
  public String referenceId;
  public String sobjectName;

  public Query() {
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
    return sobjectName;
  }

  public void setSobjectName(final String sobjectName) {
    this.sobjectName = sobjectName;
  }
}
