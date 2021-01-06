package com.salesforce.client;


import static com.salesforce.authentication.exceptions.AuthenticationException.Code.UNAUTHORIZED;
import static com.salesforce.authentication.exceptions.AuthenticationException.Code.UNKNOWN_AUTHENTICATION_FLOW;

import com.salesforce.authentication.AccessParameters;
import com.salesforce.authentication.Authentication;
import com.salesforce.authentication.jwt.JWTAuthentication;
import com.salesforce.authentication.secrets.Secrets;
import com.salesforce.authentication.secrets.SecretsUtil;
import com.salesforce.authentication.userpassword.UserPasswordAuthentication;
import com.salesforce.authentication.exceptions.AuthenticationException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;

public class SalesforceHttpClient {

  public enum AuthenticationFlow {
    USER_PASSWORD,
    JWT
  }
  private Authentication authentication;

  private Integer apiVersion = 50;
  private final String baseEndpoint = "/services/data/v"+apiVersion+".0/";
  private final String queryEndpoint = baseEndpoint+"query/?q=";
  private final String queryAllEndpoint = baseEndpoint+"query/?q=";
  private final String sobjectEndpoint = baseEndpoint + "sobjects/";
  private final String multipleRecordsEndpoint = baseEndpoint + "composite/tree/";
  private volatile AccessParameters accessParameters;
  private final BaseHTTPClient httpClient;
  private final Secrets secrets;


  public SalesforceHttpClient(AuthenticationFlow authenticationFlow)
      throws AuthenticationException, IOException {
    this.httpClient = new BaseHTTPClient();
    this.secrets = SecretsUtil.readCredentials("secrets.json");
    initClass(authenticationFlow, secrets, httpClient);
  }

  // For testing
  public SalesforceHttpClient(AuthenticationFlow authenticationFlow, BaseHTTPClient httpClient,
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

  public String get(String url)
      throws IOException, AuthenticationException {
    HttpGet getRequest = new HttpGet(url);
    getRequest.addHeader("accept", "application/json");
    getRequest.addHeader("Authorization", "Bearer " + accessParameters.accessToken);

    return executeHttpRequest(getRequest);
  }

  public String executeHttpRequest(HttpRequest request)
      throws IOException, AuthenticationException {
    String returnString = "";
    synchronized (accessParameters) {
      System.out.println("Synchronized start "+Thread.currentThread().getName());
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

      System.out.println("Synchronized stop "+Thread.currentThread().getName());
    }
    return returnString;
  }

  public String query(String query) throws IOException, AuthenticationException {
    String url = accessParameters.instanceUrl + queryEndpoint + URLEncoder.encode(query,
        StandardCharsets.UTF_8);
    return get(url);
  }

  public synchronized AccessParameters getAccessParameters() {
    return accessParameters;
  }

  //TODO: Retry JWT process and document
}
