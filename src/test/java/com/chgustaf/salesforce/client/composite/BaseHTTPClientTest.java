package com.chgustaf.salesforce.client.composite;

import static com.chgustaf.salesforce.authentication.exceptions.AuthenticationException.Code.UNAUTHORIZED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.client.BaseHTTPClient;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;



class BaseHTTPClientTest extends Mockito {

  CloseableHttpClient closeableHttpClient;
  CloseableHttpResponse closeableHttpResponse;

  @BeforeEach
  void init() {
    closeableHttpClient = mock(CloseableHttpClient.class);
    closeableHttpResponse = mock(CloseableHttpResponse.class);
  }

  @Test
  void post_success() throws IOException, AuthenticationException {
    returnStatusCode(201);
    returnBody("Accepted");
    when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
    try (MockedStatic<HttpClients> httpClientClass = Mockito.mockStatic(HttpClients.class)) {
      httpClientClass.when(HttpClients::createDefault).thenReturn(closeableHttpClient);
      BaseHTTPClient httpClient = new BaseHTTPClient();
      HttpPost postRequest = new HttpPost("https://salesforce.com");
      postRequest.addHeader("Content-Type", "application/json");
      postRequest.addHeader("Authorization", "Bearer ");
      postRequest.setEntity(new StringEntity("Test", UTF_8));
      httpClient.post(postRequest);
    }
  }

  @Test
  void post_fail() throws IOException {
    returnStatusCode(401);
    returnBody("Unauthorized");
    when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
    try (MockedStatic<HttpClients> httpClientClass = Mockito.mockStatic(HttpClients.class)) {
      httpClientClass.when(HttpClients::createDefault).thenReturn(closeableHttpClient);
      BaseHTTPClient httpClient = new BaseHTTPClient();
      HttpPost postRequest = new HttpPost("https://salesforce.com");
      postRequest.addHeader("Content-Type", "application/json");
      postRequest.addHeader("Authorization", "Bearer ");
      postRequest.setEntity(new StringEntity("Test", UTF_8));
      AuthenticationException thrownException = assertThrows(AuthenticationException.class,
          () -> httpClient.post(postRequest));
      assertEquals(UNAUTHORIZED, thrownException.getCode());
    }
  }

  @Test
  void patch_success() throws IOException, AuthenticationException {
    returnStatusCode(200);
    returnBody("OK");
    when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
    try (MockedStatic<HttpClients> httpClientClass = Mockito.mockStatic(HttpClients.class)) {
      httpClientClass.when(HttpClients::createDefault).thenReturn(closeableHttpClient);
      BaseHTTPClient httpClient = new BaseHTTPClient();
      HttpPatch patchRequest = new HttpPatch("https://salesforce.com");
      patchRequest.addHeader("Content-Type", "application/json");
      patchRequest.addHeader("Authorization", "Bearer ");
      patchRequest.setEntity(new StringEntity("Test", UTF_8));
      httpClient.patch(patchRequest);
    }
  }

  @Test
  void get_success() throws IOException, AuthenticationException {
    returnStatusCode(200);
    returnBody("OK");
    when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
    try (MockedStatic<HttpClients> httpClientClass = Mockito.mockStatic(HttpClients.class)) {
      httpClientClass.when(HttpClients::createDefault).thenReturn(closeableHttpClient);
      BaseHTTPClient httpClient = new BaseHTTPClient();
      HttpGet getRequest = new HttpGet("https://salesforce.com");
      getRequest.addHeader("Content-Type", "application/json");
      getRequest.addHeader("Authorization", "Bearer ");
      httpClient.get(getRequest);
    }
  }

  @Test
  void delete_success() throws IOException, AuthenticationException {
    returnStatusCode(200);
    returnBody("OK");
    when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
    try (MockedStatic<HttpClients> httpClientClass = Mockito.mockStatic(HttpClients.class)) {
      httpClientClass.when(HttpClients::createDefault).thenReturn(closeableHttpClient);
      BaseHTTPClient httpClient = new BaseHTTPClient();
      HttpGet getRequest = new HttpGet("https://salesforce.com");
      getRequest.addHeader("Content-Type", "application/json");
      getRequest.addHeader("Authorization", "Bearer ");
      httpClient.get(getRequest);
    }
  }

  private void returnStatusCode(int statusCode) {
    StatusLine statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(statusCode);
    when(closeableHttpResponse.getStatusLine()).thenReturn(statusLine);
  }

  private void returnBody(String responseBody) throws IOException {
    InputStream stubInputStream =
        IOUtils.toInputStream(responseBody, "UTF-8");
    HttpEntity httpEntity = mock(HttpEntity.class);
    when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
    when(httpEntity.getContent()).thenReturn(stubInputStream);
  }
}
