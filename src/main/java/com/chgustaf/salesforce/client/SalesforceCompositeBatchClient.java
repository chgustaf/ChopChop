package com.chgustaf.salesforce.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import java.io.IOException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class SalesforceCompositeBatchClient {

  private SalesforceHttpClient client;

  private String compositeSobjectEndpoint;
  private String compositeEndpoint;
  private String compositeBatchEndpoint;

  public SalesforceCompositeBatchClient() throws IOException,
                                                 AuthenticationException {
    client = new SalesforceHttpClient();
    initEndpoints();
  }

  private void initEndpoints() {
    final String instanceUrl = client.getAccessParameters().instanceUrl;
    final Integer apiVersion = 50;
    final String baseEndpoint = instanceUrl + "/services/data/v" + apiVersion + ".0/";
    compositeSobjectEndpoint = baseEndpoint + "composite/sobjects";
    compositeBatchEndpoint = baseEndpoint + "composite/batch";
    compositeEndpoint = baseEndpoint + "composite";
    final String readEndpoint = instanceUrl + baseEndpoint;
  }

  public String compositeCall(String requestString)
      throws IOException, AuthenticationException {
    HttpPost postRequest = new HttpPost(compositeEndpoint);
    postRequest.addHeader("Content-Type", "application/json");
    postRequest.addHeader("Authorization", "Bearer " + client.getAccessParameters().accessToken);
    postRequest.setEntity(new StringEntity(requestString, UTF_8));
    return client.executeHttpRequest(postRequest);
  }

  public String compositeBatchCall(String requestString)
      throws IOException, AuthenticationException {
    HttpPost postRequest = new HttpPost(compositeBatchEndpoint);
    postRequest.addHeader("Content-Type", "application/json");
    postRequest.addHeader("Authorization", "Bearer " + client.getAccessParameters().accessToken);
    postRequest.setEntity(new StringEntity(requestString, UTF_8));
    return client.executeHttpRequest(postRequest);
  }
}
