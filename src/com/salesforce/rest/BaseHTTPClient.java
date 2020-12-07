package com.salesforce.rest;

import static com.salesforce.exceptions.AuthenticationException.Code.UNAUTHORIZED;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.salesforce.exceptions.AuthenticationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BaseHTTPClient {

  public Map<String, Set<Integer>> acceptableResponseCodes;

  public BaseHTTPClient() {
    acceptableResponseCodes = new HashMap<>();
    acceptableResponseCodes.put("GET", new HashSet<>(Arrays.asList(200)));
    acceptableResponseCodes.put("POST", new HashSet<>(Arrays.asList(200, 201)));
    acceptableResponseCodes.put("PATCH", new HashSet<>(Arrays.asList(200)));
    acceptableResponseCodes.put("DELETE", new HashSet<>(Arrays.asList(200, 202, 204)));
  }

  public String post(HttpPost postRequest) throws IOException, AuthenticationException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    printRequest(postRequest);
    HttpResponse response = httpClient.execute(postRequest);
    checkResponse(response, "POST");
    String responseText = getResponseText(response);
    httpClient.close();
    return responseText;
  }

  public String get(HttpGet getRequest) throws IOException, AuthenticationException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(getRequest);
    checkResponse(response, "GET");
    String text = getResponseText(response);
    httpClient.close();
    return text;
  }

  public String delete(HttpDelete deleteRequest) throws IOException, AuthenticationException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    printRequest(deleteRequest);
    HttpResponse response = httpClient.execute(deleteRequest);
    checkResponse(response, "DELETE");
    //String responseText = getResponseText(response);
    httpClient.close();
    return "";
  }

  public String patch(HttpPatch patchRequest) throws IOException, AuthenticationException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(patchRequest);
    checkResponse(response, "PATCH");
    String responseText = getResponseText(response);
    httpClient.close();
    return responseText ;
  }

  private void checkResponse(HttpResponse response, String httpMethod) throws AuthenticationException {
    if (response.getStatusLine().getStatusCode() == 401) {
      throw new AuthenticationException(UNAUTHORIZED, "401 Unauthorized during POST");
    }

    if (!acceptableResponseCodes.get(httpMethod).contains(response.getStatusLine().getStatusCode())) {
      throw new RuntimeException("Request failed : HTTP error code " + response.getStatusLine().getStatusCode() + " "+response.getStatusLine().getReasonPhrase());
    }
  }

  private String getResponseText(HttpResponse response) throws IOException {
    if (response != null) {
      return IOUtils.toString(
          response.getEntity().getContent(),
          UTF_8.name());
    }
    return "";
  }

  public void printRequest(HttpPost postRequest) {
    System.out.println("Sending POST request to " + postRequest.getURI());
    System.out.println("Available headers:");
    for (Header header : postRequest.getAllHeaders()) {
      System.out.println(header.getName() + ": " + header.getValue());
    }
    try {
      System.out.println(EntityUtils.toString(postRequest.getEntity()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printRequest(HttpDelete deleteRequest) {
    System.out.println("Sending DELETE request to " + deleteRequest.getURI());
    System.out.println("Available headers:");
    for (Header header : deleteRequest.getAllHeaders()) {
      System.out.println(header.getName() + ": " + header.getValue());
    }
  }
}
