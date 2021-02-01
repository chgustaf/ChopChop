package com.chgustaf.examples.caseupdater.exampleclient.domain;

import com.chgustaf.salesforce.client.composite.domain.Record;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
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
