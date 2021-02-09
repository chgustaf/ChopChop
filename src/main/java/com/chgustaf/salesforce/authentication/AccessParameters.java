package com.chgustaf.salesforce.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessParameters {

  @JsonProperty("access_token")
  private String accessToken;
  private String scope;
  @JsonProperty("instance_url")
  private String instanceUrl;
  private String id;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("issued_at")
  private String issuedAt;
  private String signature;

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

  public String getAccessToken() {
    return accessToken;
  }

  private void setAccessToken(final String accessToken) {
    this.accessToken = accessToken;
  }

  private String getScope() {
    return scope;
  }

  private void setScope(final String scope) {
    this.scope = scope;
  }

  public String getInstanceUrl() {
    return instanceUrl;
  }

  private void setInstanceUrl(final String instanceUrl) {
    this.instanceUrl = instanceUrl;
  }

  private String getId() {
    return id;
  }

  private void setId(final String id) {
    this.id = id;
  }

  private String getTokenType() {
    return tokenType;
  }

  private void setTokenType(final String tokenType) {
    this.tokenType = tokenType;
  }

  private String getIssuedAt() {
    return issuedAt;
  }

  private void setIssuedAt(final String issuedAt) {
    this.issuedAt = issuedAt;
  }

  private String getSignature() {
    return signature;
  }

  private void setSignature(final String signature) {
    this.signature = signature;
  }
}

