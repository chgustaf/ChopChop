package com.salesforce.authentication;

import com.salesforce.authentication.secrets.Secrets;
import com.salesforce.authentication.exceptions.AuthenticationException;
import com.salesforce.client.BaseHTTPClient;
import java.io.IOException;
import org.apache.http.client.methods.HttpPost;

public abstract class Authentication {

  public final String LOGIN_URL = "https://login.salesforce.com/services/oauth2/token";

  protected BaseHTTPClient httpClient;
  protected Secrets secrets;

  protected Authentication(Secrets secrets, BaseHTTPClient httpClient) {
    this.secrets = secrets;
    this.httpClient = httpClient;
  }

  public abstract AccessParameters authenticate() throws IOException, AuthenticationException;


  public String post(HttpPost post) throws IOException, AuthenticationException {
    return httpClient.post(post);
  }
}
