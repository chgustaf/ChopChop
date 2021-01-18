package com.chgustaf.salesforce.client.composite.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;

public class BatchRequest {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String binaryPartName;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String getBinaryPartNameAlias;
  String method; //required

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonRawValue
  String richInput;

  String url; //required
  Type type;

  String referenceId;

  @JsonIgnore
  String id;

  public enum Type {
    LIMITS,
    SOBJECT,
    QUERY,
    QUERYALL,
    SEARCH,
    CONNECT,
    CHATTER
  }

  BatchRequest(final Type type, final String binaryPartName,
               final String getBinaryPartNameAlias,
               final String method,
               final String richInput, final String url, final String referenceId,
               final String id) {
    this.binaryPartName = binaryPartName;
    this.getBinaryPartNameAlias = getBinaryPartNameAlias;
    this.method = method;
    this.richInput = richInput;
    this.url = url;
    this.type = type;
    this.referenceId = referenceId;
    this.id = id;
  }

  @JsonIgnore
  public Type getType() {
    return type;
  }

  public void setType(final Type type) {
    this.type = type;
  }

  public String getBinaryPartName() {
    return binaryPartName;
  }

  public void setBinaryPartName(final String binaryPartName) {
    this.binaryPartName = binaryPartName;
  }

  public String getGetBinaryPartNameAlias() {
    return getBinaryPartNameAlias;
  }

  public void setGetBinaryPartNameAlias(final String getBinaryPartNameAlias) {
    this.getBinaryPartNameAlias = getBinaryPartNameAlias;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(final String method) {
    this.method = method;
  }

  public String getRichInput() {
    return richInput;
  }

  public void setRichInput(final String richInput) {
    this.richInput = richInput;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  @JsonIgnore
  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(final String referenceId) {
    this.referenceId = referenceId;
  }

  @JsonIgnore
  public String getId() {
    return id;
  }
}
