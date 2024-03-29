package com.chgustaf.salesforce.authentication.jwt;


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
public class JWTAuthentication extends Authentication {

  public JWTAuthentication(final Secrets secrets, final BaseHTTPClient httpClient) {
    super(secrets, httpClient);
  }

  public String createJWT() throws AuthenticationException, IOException {
    JWTFactory authentication = new JWTFactory(secrets);
    return authentication.createJWT();
  }

  public HttpEntity getJWTEntity(String jwt) {
    List<NameValuePair> nvps = new ArrayList<>();
    nvps.add(new BasicNameValuePair("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer"));
    nvps.add(new BasicNameValuePair("assertion", jwt));
    return new UrlEncodedFormEntity(nvps, UTF_8);
  }

  public AccessParameters getAccessToken(String loginURL, String jwt)
      throws IOException, AuthenticationException, TransactionException {
    HttpPost httpPost = new HttpPost(loginURL);
    httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
    httpPost.setEntity(getJWTEntity(jwt));
    String returnJson = post(httpPost);
    ObjectMapper objectMapper = new ObjectMapper();
    AccessParameters accessParameters = objectMapper.readValue(returnJson, AccessParameters.class);
    return accessParameters;
  }

  @Override
  public AccessParameters authenticate()
      throws IOException, AuthenticationException, TransactionException {
    String jwt = createJWT();
    return getAccessToken(LOGIN_URL, jwt);
  }
}
