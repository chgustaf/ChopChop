package com.examples.caseupdater.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(as=Account.class)
public class Account extends Record {

  public String name;
  public String description;

  public Account() {
    super(Account.class);
  }

  public Account(String name) {
    this();
    this.name = name;
  }

  @Override
  @JsonIgnore
  public List<String> getAllFields() {
    return super.getAllFieldsHelper(this.getClass());
  }

  @JsonIgnore
  public String getJSON() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    return mapper.writeValueAsString(this);
  }

  @Override
  public Class getClazz() {
    return this.getClass();
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Account{" +
           "id='" + id + '\'' +
           ", name='" + name + '\'' +
           ", description='" + description + '\'' +
           ", statusCode=" + statusCode +
           ", attributes=" + attributes +
           ", mapper=" + mapper +
           ", referenceId='" + getReferenceId()+ '\'' +
           ", sobjectName='" + sobjectName + '\'' +
           ", success=" + success +
           '}';
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }
}
