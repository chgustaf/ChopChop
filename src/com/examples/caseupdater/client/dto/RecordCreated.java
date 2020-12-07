package com.examples.caseupdater.client.dto;

import java.util.Arrays;

public class RecordCreated {
  public String id;
  public Boolean success;
  public String[] errors;

  @Override
  public String toString() {
    return "RecordCreated{" +
           "id='" + id + '\'' +
           ", success=" + success +
           ", errors=" + Arrays.toString(errors) +
           '}';
  }
}
