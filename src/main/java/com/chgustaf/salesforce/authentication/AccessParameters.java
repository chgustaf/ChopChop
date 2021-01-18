package com.chgustaf.salesforce.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessParameters {

  @JsonProperty("access_token")
  public String accessToken;
  public String scope;
  @JsonProperty("instance_url")
  public String instanceUrl;
  public String id;
  @JsonProperty("token_type")
  public String tokenType;
  @JsonProperty("issued_at")
  public String issuedAt;
  public String signature;

  @Override
  public String toString() {
    return "AccessParameters{" +
           "accessToken='" + accessToken + '\'' +
           ", scope='" + scope + '\'' +
           ", instanceUrl='" + instanceUrl + '\'' +
           ", id='" + id + '\'' +
           ", tokenType='" + tokenType + '\'' +
           '}';
  }
}

