package com.chgustaf.salesforce.authentication.userpassword;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.chgustaf.salesforce.authentication.AccessParameters;
import com.chgustaf.salesforce.authentication.Authentication;
import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.secrets.Secrets;
import com.chgustaf.salesforce.client.BaseHTTPClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

public class UserPasswordAuthentication extends Authentication {

  public UserPasswordAuthentication(final Secrets secrets, final BaseHTTPClient httpClient) {
    super(secrets, httpClient);
  }

  public AccessParameters getAccessToken(String loginURL)
      throws IOException, AuthenticationException, TransactionException {
    HttpPost httpPost = new HttpPost(loginURL);
    httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
    httpPost.setEntity(getUserPasswordEntity());
    String returnJson = post(httpPost);
    ObjectMapper objectMapper = new ObjectMapper();
    AccessParameters accessParameters = objectMapper.readValue(returnJson, AccessParameters.class);
    return accessParameters;
  }

  @Override
  public AccessParameters authenticate()
      throws IOException, AuthenticationException, TransactionException {
    return getAccessToken(LOGIN_URL);
  }

  private HttpEntity getUserPasswordEntity() {
    List<NameValuePair> nvps = new ArrayList<>();
    nvps.add(new BasicNameValuePair("grant_type", "password"));
    nvps.add(new BasicNameValuePair("client_id", secrets.getConsumerKey()));
    nvps.add(new BasicNameValuePair("client_secret", secrets.getConsumerSecret()));
    nvps.add(new BasicNameValuePair("username", secrets.getUsername()));
    nvps.add(new BasicNameValuePair("password", secrets.getPassword()+secrets.getSecurityToken()));
    return new UrlEncodedFormEntity(nvps, UTF_8);
  }


}
