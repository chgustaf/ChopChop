package com.chgustaf.examples.caseupdater.exampleclient.domain;

import com.chgustaf.salesforce.client.composite.domain.Record;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Case extends Record {

  public String subject;
  public String accountId;

  public Case() {
    super(Case.class);
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(final String subject) {
    this.subject = subject;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(final String accountId) {
    this.accountId = accountId;
  }

  @Override
  public String toString() {
    return "Case{" +
           "subject='" + subject + '\'' +
           ", accountId='" + accountId + '\'' +
           '}';
  }
}
