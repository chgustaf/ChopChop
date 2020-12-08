package com.salesforce.rest;


import static com.salesforce.exceptions.AuthenticationException.Code.UNKNOWN_AUTHENTICATION_FLOW;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.authentication.AccessParameters;
import com.salesforce.authentication.Authentication;
import com.salesforce.authentication.jwt.JWTAuthentication;
import com.salesforce.authentication.secrets.Secrets;
import com.salesforce.authentication.secrets.SecretsUtil;
import com.salesforce.authentication.userpassword.UserPasswordAuthentication;
import com.salesforce.exceptions.AuthenticationException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

public class SalesforceClient {

  public enum AuthenticationFlow {
    USER_PASSWORD,
    JWT
  }
  private Authentication authentication;

  public Integer apiVersion = 50;
  public final String baseEndpoint = "/services/data/v"+apiVersion+".0/";
  public final String queryEndpoint = baseEndpoint+"query/?q=";
  public final String queryAllEndpoint = baseEndpoint+"query/?q=";
  public final String sobjectEndpoint = baseEndpoint + "sobjects/";
  public final String multipleRecordsTreeEndpoint = baseEndpoint + "composite/tree/";
  public final String multipleRecordsEndpoint = baseEndpoint + "composite/sobjects";
  public AccessParameters accessParameters;
  public final BaseHTTPClient httpClient;
  public final Secrets secrets;

  public SalesforceClient(AuthenticationFlow authenticationFlow)
      throws AuthenticationException, IOException {
    this.httpClient = new BaseHTTPClient();
    this.secrets = SecretsUtil.readCredentials("secrets.json");
    initClass(authenticationFlow, secrets, httpClient);
  }

  // For testing
  public SalesforceClient(AuthenticationFlow authenticationFlow, BaseHTTPClient httpClient,
                          Secrets secrets)
      throws IOException, AuthenticationException {
    this.httpClient = httpClient;
    this.secrets = secrets;
    this.apiVersion = secrets.getApiVersion();
    initClass(authenticationFlow, secrets, httpClient);
  }

  private void initClass(AuthenticationFlow authenticationFlow, Secrets secrets,
                         BaseHTTPClient httpClient)
      throws AuthenticationException, IOException {
    switch (authenticationFlow) {
      case USER_PASSWORD:
        authentication = new UserPasswordAuthentication(secrets, httpClient);
        break;
      case JWT:
        authentication = new JWTAuthentication(secrets, httpClient);
        break;
      default:
        throw new AuthenticationException(
            UNKNOWN_AUTHENTICATION_FLOW,
            "Undefined authentication flow provided");
    }
    accessParameters = authentication.authenticate();
    System.out.println("Access Token " + accessParameters);
  }

  // TODO: Finalize on a uniform return string
  public String get(String url)
      throws IOException, AuthenticationException {

    request.setEndpoint(url);

    HttpGet getRequest = new HttpGet(url);
    getRequest.addHeader("accept", "application/json");
    getRequest.addHeader("Authorization", "Bearer " + accessParameters.accessToken);
    return new ObjectMapper().writeValueAsString(executeHttpRequest(getRequest));
  }

  public String getSingleRecord(String recordId, String objectName) {
    Transaction transaction = new Transaction();
    Request request = new Request();
  }

  public String postMultipleRecords(String objectName, String body)
      throws IOException, AuthenticationException {
    HttpPost postRequest = new HttpPost(accessParameters.instanceUrl + multipleRecordsTreeEndpoint
                                        + objectName);
    postRequest.addHeader("Content-Type", "application/json");
    postRequest.addHeader("Authorization", "Bearer " + accessParameters.accessToken);
    postRequest.setEntity(new StringEntity(body));

    TransactionResponse transactionResponse = executeHttpRequest(postRequest);

    new ResponseBuilder().setAction(Response.Action.POST)

    return new ObjectMapper().writeValueAsString();
  }

  public String postSingleRecord(String objectName, String body) throws IOException, AuthenticationException {
    HttpPost postRequest = new HttpPost(accessParameters.instanceUrl + sobjectEndpoint + objectName);
    postRequest.addHeader("Content-Type", "application/json");
    postRequest.addHeader("Authorization", "Bearer " + accessParameters.accessToken);
    postRequest.setEntity(new StringEntity(body));
    return new ObjectMapper().writeValueAsString(executeHttpRequest(postRequest));
  }

  public String patch(String url, String body) throws IOException, AuthenticationException {
    HttpPatch patchRequest = new HttpPatch(url);
    patchRequest.addHeader("accept", "application/json");
    patchRequest.addHeader("Authorization", "Bearer " + accessParameters.accessToken);
    return new ObjectMapper().writeValueAsString(executeHttpRequest(patchRequest));
  }

  public String deleteSingleRecord(String objectName, String recordId) throws IOException,
                                                               AuthenticationException {
    HttpDelete deleteRequest =
        new HttpDelete(accessParameters.instanceUrl + sobjectEndpoint + objectName +"/" + recordId);
    deleteRequest.addHeader("X", "DELETE");
    deleteRequest.addHeader("Authorization", "Bearer " + accessParameters.accessToken);
    return new ObjectMapper().writeValueAsString(executeHttpRequest(deleteRequest));
  }

  public String deleteMultipleRecords(List<String> recordIds, boolean allOrNone)
      throws IOException, AuthenticationException {
    HttpDelete deleteRequest =
        new HttpDelete(accessParameters.instanceUrl + multipleRecordsEndpoint+"?ids="+String.join(
            ",",
        recordIds)+"&allOrNone="+allOrNone);
    deleteRequest.addHeader("X", "DELETE");
    deleteRequest.addHeader("Authorization", "Bearer " + accessParameters.accessToken);
    String responseString =
        new ObjectMapper().writeValueAsString(executeHttpRequest(deleteRequest));
    return responseString;
  }
/*
  private String executeHttpRequest(HttpRequest request)
      throws IOException, AuthenticationException {
    TransactionResponse transactionResponse;
    int maxRetries = 1;
    for (int i = 0; i <= maxRetries; i++) {
      try {
        if (request instanceof HttpDelete) {
          returnString = httpClient.delete((HttpDelete) request);
        } else if (request instanceof HttpGet) {
          returnString = httpClient.get((HttpGet) request);
        } else if (request instanceof HttpPatch) {
          returnString = httpClient.patch((HttpPatch) request);
        } else if (request instanceof HttpPost) {
          returnString = httpClient.post((HttpPost) request);
        }
        break;
      } catch (AuthenticationException authenticationException) {
        if (authenticationException.getCode().equals(UNAUTHORIZED)) {
          System.out.println("Re-Authenticating");
          accessParameters = authentication.authenticate();
          request.removeHeaders("Authorization");
          request.addHeader("Authorization", accessParameters.accessToken);
        }
      }
    }
    return returnString;
  }
*/

  private TransactionResponse executeHttpRequest(HttpRequest request)
      throws IOException, AuthenticationException {
    TransactionResponse transactionResponse = null;
    int maxRetries = 1;
    for (int i = 0; i <= maxRetries; i++) {
      if (request instanceof HttpDelete) {
        transactionResponse = httpClient.delete((HttpDelete) request);
      } else if (request instanceof HttpGet) {
        transactionResponse = httpClient.get((HttpGet) request);
      } else if (request instanceof HttpPatch) {
        transactionResponse = httpClient.patch((HttpPatch) request);
      } else if (request instanceof HttpPost) {
        transactionResponse = httpClient.post((HttpPost) request);
      }

      if (transactionResponse.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
        System.out.println("Re-Authenticating");
        accessParameters = authentication.authenticate();
        request.removeHeaders("Authorization");
        request.addHeader("Authorization", accessParameters.accessToken);
      }
    }
      return transactionResponse;
  }


  public String query(String query) throws IOException, AuthenticationException {
    String url = accessParameters.instanceUrl + queryEndpoint + URLEncoder.encode(query,
        StandardCharsets.UTF_8);
    return get(url);
  }

  public String queryAll(String query) throws IOException, AuthenticationException {
    String url = accessParameters.instanceUrl + queryAllEndpoint + URLEncoder.encode(query,
        UTF_8);
    return get(url);
  }

  public Response toResponse(TransactionResponse transactionResponse) {
    if (transactionResponse.getMethod().equals(TransactionResponse.Action.POST)) {

    } else if (transactionResponse.getMethod().equals(TransactionResponse.Action.GET)) {

    } else if (transactionResponse.getMethod().equals(TransactionResponse.Action.DELETE)) {

    } else if (transactionResponse.getMethod().equals(TransactionResponse.Action.PATCH)) {

    }
    new ResponseBuilder().setAction(Response.Action.POST).set
    transactionResponse.getStatusCode()
  }

  //TODO: Retry JWT process and document


}
