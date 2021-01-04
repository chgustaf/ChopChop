package com.examples.caseupdater.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;

public class Case extends Record {

  public String id;
  public String subject;
  public String accountId;

  public Case() {
    super("Case");
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
}
