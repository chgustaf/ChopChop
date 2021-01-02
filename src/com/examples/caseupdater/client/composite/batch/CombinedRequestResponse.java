package com.examples.caseupdater.client.composite.batch;

import com.examples.caseupdater.client.composite.InnerResult;

public class CombinedRequestResponse {

  public BatchRequest request;
  public InnerResult result;
  public Integer statusCode;


  public CombinedRequestResponse(
      final BatchRequest request, final InnerResult result, final Integer statusCode) {
    this.request = request;
    this.result = result;
    this.statusCode = statusCode;
  }

  public BatchRequest getRequest() {
    return request;
  }

  public void setRequest(final BatchRequest request) {
    this.request = request;
  }

  public InnerResult getResult() {
    return result;
  }

  public void setResult(final InnerResult result) {
    this.result = result;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final Integer statusCode) {
    this.statusCode = statusCode;
  }
}
