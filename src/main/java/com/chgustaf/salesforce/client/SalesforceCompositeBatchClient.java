package com.chgustaf.salesforce.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import java.io.IOException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class SalesforceCompositeBatchClient {

  private SalesforceHttpClient salesforceHttpClient;

  String compositeBatchEndpoint;

  public SalesforceCompositeBatchClient() throws IOException,
                                                 AuthenticationException, TransactionException {
    this(new SalesforceHttpClient());
  }

  public SalesforceCompositeBatchClient(SalesforceHttpClient salesforceHttpClient) {
    this.salesforceHttpClient = salesforceHttpClient;
    initEndpoints();
  }

  private void initEndpoints() {
    final String instanceUrl = salesforceHttpClient.getAccessParameters().getInstanceUrl();
    final Integer apiVersion = 50;
    final String baseEndpoint = instanceUrl + "/services/data/v" + apiVersion + ".0/";
    compositeBatchEndpoint = baseEndpoint + "composite/batch";
  }

  public String compositeBatchCall(String requestString)
      throws IOException, AuthenticationException, TransactionException {
    HttpPost postRequest = new HttpPost(compositeBatchEndpoint);
    postRequest.addHeader("Content-Type", "application/json");
    postRequest.addHeader("Authorization", "Bearer " + salesforceHttpClient.getAccessParameters().getAccessToken());
    postRequest.setEntity(new StringEntity(requestString, UTF_8));
    return salesforceHttpClient.executeHttpRequest(postRequest);
  }
}
