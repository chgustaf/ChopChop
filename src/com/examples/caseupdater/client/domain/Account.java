package com.examples.caseupdater.client.domain;

import com.examples.caseupdater.client.dto.Attributes;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

public class Account implements Record{

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String id;
  private String name;

  private Attributes attributes;

  public Account() {
    this.attributes = new Attributes("Account", UUID.randomUUID().toString());
  }

  public Account(String name) {
    this.name = name;
    this.attributes = new Attributes("Account", UUID.randomUUID().toString());
  }

  public Account(final String id, final String name, final Attributes attributes) {
    this.id = id;
    this.name = name;
    this.attributes = attributes;
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Attributes getAttributes() {
    return attributes;
  }

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
