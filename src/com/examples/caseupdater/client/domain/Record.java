package com.examples.caseupdater.client.domain;

public interface Record {

  String getId();

  void setId(String id);

  default boolean inserted() {
    return (this.getId() != null && this.getId().trim() == "");
  }
}
