package com.examples.caseupdater.client.domain;

import com.examples.caseupdater.client.dto.Attributes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

public class Account implements Record {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String id;
  private String name;
  private String aFieldThatDoesNotExist;

  private Integer statusCode;
  private Attributes attributes;
  private ObjectMapper mapper;

  private String referenceId;
  private String sobjectName;

  private Boolean success;

  public Account() {
    this.attributes = new Attributes("Account", UUID.randomUUID().toString());
    this.mapper = new ObjectMapper();
  }

  public Account(String name) {
    this();
    this.name = name;
    this.attributes = new Attributes("Account", UUID.randomUUID().toString());
  }

  public Account(final String id, final String sobjectName, final String referenceId,
                 final String json, final Integer statusCode) {
    this();
    this.id = id;
    this.name = name;
    this.attributes = new Attributes(sobjectName, referenceId);
    //mapper.readValue(json, this);
    this.statusCode = statusCode;
  }



  public String getId() {
    return id;
  }

  @JsonIgnore
  @Override
  public String getJSON() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }

  @JsonIgnore
  @Override
  public Integer getStatusCode() {
    return statusCode;
  }

  @Override
  public void setSuccess(final Boolean success) {
    this.success = success;
  }

  @Override
  public void setReferenceId(final String referenceId) {
    this.referenceId = referenceId;
  }

  @Override
  public void setStatusCode(final Integer statusCode) {
    this.statusCode = statusCode;
  }

  public void setId(final String id) {
    this.id = id;
  }

  @Override
  public void setJSON(final String json) {

  }

  public String getName() {
    return name;
  }

  @JsonIgnore
  @Override
  public String getSObjectName() {
    return this.getClass().getSimpleName();
  }

  @JsonIgnore
  @Override
  public String getReferenceId() {
    return attributes.getReferenceId();
  }

  @JsonIgnore
  @Override
  public Boolean getSuccess() {
    return success;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @JsonIgnore
  public Attributes getAttributes() {
    return attributes;
  }

  @JsonIgnore
  public void setAttributes(final Attributes attributes) {
    this.attributes = attributes;
  }

  @Override
  public String toString() {
    return "Account{" +
           "id='" + id + '\'' +
           ", name='" + name + '\'' +
           ", attributes=" + attributes +
           '}';
  }

}
