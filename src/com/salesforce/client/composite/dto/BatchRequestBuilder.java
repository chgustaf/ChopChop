package com.salesforce.client.composite.dto;

public class BatchRequestBuilder {

  private BatchRequest.Type type;
  private String binaryPartName;
  private String getBinaryPartNameAlias;
  private String method;
  private String richInput;
  private String url;
  private String referenceId;
  private String id;

  public BatchRequestBuilder setType(final BatchRequest.Type type) {
    this.type = type;
    return this;
  }

  public BatchRequestBuilder setBinaryPartName(final String binaryPartName) {
    this.binaryPartName = binaryPartName;
    return this;
  }

  public BatchRequestBuilder setGetBinaryPartNameAlias(final String getBinaryPartNameAlias) {
    this.getBinaryPartNameAlias = getBinaryPartNameAlias;
    return this;
  }

  public BatchRequestBuilder setMethod(final String method) {
    this.method = method;
    return this;
  }

  public BatchRequestBuilder setRichInput(final String richInput) {
    this.richInput = richInput;
    return this;
  }

  public BatchRequestBuilder setUrl(final String url) {
    this.url = url;
    return this;
  }

  public BatchRequestBuilder setReferenceId(final String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  public BatchRequestBuilder setId(final String id) {
    this.id = id;
    return this;
  }


  public BatchRequest createBatchRequest() {
    return new BatchRequest(type, binaryPartName, getBinaryPartNameAlias, method,
        richInput, url, referenceId, id);
  }
}