package com.chgustaf.examples.caseupdater.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.chgustaf.salesforce.client.composite.domain.Record;
import java.util.List;

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
  public List<String> getAllFields() {
    return super.getAllFieldsHelper(this.getClass());
  }

  @Override
  @JsonIgnore
  public String getJSON() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    return mapper.writeValueAsString(this);
  }

  @Override
  public Class getClazz() {
    return null;
  }

  @Override
  public String toString() {
    return "Case{" +
           "subject='" + subject + '\'' +
           ", accountId='" + accountId + '\'' +
           '}';
  }
}
