package com.examples.caseupdater.client.composite;

import java.util.Map;

public class CompositeResponse {

  public int httpStatusCode;
  public Body body;
  public String referenceId;
  public Map<String, String> httpHeaders;

  @Override
  public String toString() {
    return "CompositeResponse{" +
           "httpStatusCode=" + httpStatusCode +
           ", body=" + body +
           ", referenceId='" + referenceId + '\'' +
           ", httpHeaders=" + httpHeaders +
           '}';
  }
}
