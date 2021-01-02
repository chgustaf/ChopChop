package com.examples.caseupdater.client.domain;

public class SObject<T> {

  private T payload;

  public SObject(T payload) {
    this.payload = payload;
  }

  public T getSObject() {
    return payload;
  }
}
