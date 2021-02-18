package com.chgustaf.examples.caseupdater.exampleclient.domain;

import com.chgustaf.salesforce.client.composite.domain.Record;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class Case extends Record {

  @JsonProperty("Subject")
  public String subject;
  @JsonProperty("AccountId")
  public String accountId;
  @JsonProperty("Origin")
  public String origin;

  public Case() {
    super(Case.class);
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(final String subject) {
    this.subject = subject;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(final String origin) {
    this.origin = origin;
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
