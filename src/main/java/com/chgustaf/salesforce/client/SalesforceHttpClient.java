package com.chgustaf.salesforce.client;

import static com.chgustaf.salesforce.authentication.exceptions.AuthenticationException.Code.UNAUTHORIZED;
import static com.chgustaf.salesforce.authentication.exceptions.AuthenticationException.Code.UNKNOWN_AUTHENTICATION_FLOW;

import com.chgustaf.salesforce.authentication.AccessParameters;
import com.chgustaf.salesforce.authentication.Authentication;
import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.jwt.JWTAuthentication;
import com.chgustaf.salesforce.authentication.secrets.Secrets;
import com.chgustaf.salesforce.authentication.secrets.SecretsUtil;
import com.chgustaf.salesforce.authentication.userpassword.UserPasswordAuthentication;
import java.io.IOException;
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
  private volatile AccessParameters accessParameters;
  private final BaseHTTPClient httpClient;

  public SalesforceHttpClient()
      throws AuthenticationException, IOException {
    this(new BaseHTTPClient(), SecretsUtil.readCredentials("secrets.json"));
  }

  // For testing
  SalesforceHttpClient(BaseHTTPClient httpClient,
                              Secrets secrets)
      throws IOException, AuthenticationException {
    this.httpClient = httpClient;
    initClass(getAuthenticationFlow(secrets), secrets, httpClient);
  }

  void initClass(AuthenticationFlow authenticationFlow,
                         Secrets secrets,
                         BaseHTTPClient httpClient)
      throws AuthenticationException, IOException {
    authentication = determineFlow(authenticationFlow, secrets, httpClient);
    accessParameters = authentication.authenticate();
    System.out.println("Access Token " + accessParameters);
  }

  AccessParameters authenticate(Authentication authentication) throws IOException, AuthenticationException {
    return authentication.authenticate();
  }

  Authentication determineFlow(AuthenticationFlow authenticationFlow,
                             Secrets secrets,
                             BaseHTTPClient httpClient) {
    Authentication authentication = null;
    switch (authenticationFlow) {
      case USER_PASSWORD:
        System.out.println("Username-Password Authentication selected");
        authentication = new UserPasswordAuthentication(secrets, httpClient);
        break;
      case JWT:
        System.out.println("JWT Authentication selected");
        authentication = new JWTAuthentication(secrets, httpClient);
        break;
    }
    return authentication;
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
            request.addHeader("Authorization", accessParameters.getAccessToken());
          }
        }
      }

      System.out.println("Synchronized stop "+Thread.currentThread().getName());
    }
    return returnString;
  }

  synchronized AccessParameters getAccessParameters() {
    return accessParameters;
  }

  AuthenticationFlow getAuthenticationFlow(Secrets secrets) throws AuthenticationException {
    if (secrets.getAuthenticationMethod().toUpperCase().trim().equals("JWT")) {
      return AuthenticationFlow.JWT;
    } else if (secrets.getAuthenticationMethod().toUpperCase().trim().equals("USERNAME_PASSWORD")) {
      return AuthenticationFlow.USER_PASSWORD;
    }
    throw new AuthenticationException(
        UNKNOWN_AUTHENTICATION_FLOW,
        "Unknown authentication flow: " + secrets.getAuthenticationMethod());
  }
}
