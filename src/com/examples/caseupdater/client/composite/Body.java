package com.examples.caseupdater.client.composite;

import java.util.Arrays;

public class Body {

  public String id;
  public boolean success;
  public String[] errors;
  public String message;
  public String errorCode;
  public String[] fields;


  @Override
  public String toString() {
    return "Body{" +
           "id='" + id + '\'' +
           ", success=" + success +
           ", errors=" + Arrays.toString(errors) +
           ", message='" + message + '\'' +
           ", errorCode='" + errorCode + '\'' +
           ", fields=" + Arrays.toString(fields) +
           '}';
  }
}
