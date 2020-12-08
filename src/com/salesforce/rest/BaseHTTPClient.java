package com.salesforce.rest;

import static com.salesforce.exceptions.AuthenticationException.Code.UNAUTHORIZED;
import static com.salesforce.rest.TransactionResponse.Action.DELETE;
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

  public TransactionResponse post(HttpPost postRequest) throws IOException, AuthenticationException {
    CloseableHttpClient httpClient = getClosableHttpClient();
    printRequest(postRequest);
    HttpResponse response = httpClient.execute(postRequest);
    retryIfNeeded(response, "POST");
    String responseText = getResponseText(response);
    httpClient.close();
    return new TransactionResponseBuilder().createTransactionResponse();
  }

  public TransactionResponse get(HttpGet getRequest) throws IOException, AuthenticationException {
    CloseableHttpClient httpClient = getClosableHttpClient();
    HttpResponse response = httpClient.execute(getRequest);
    retryIfNeeded(response, "GET");
    String text = getResponseText(response);
    httpClient.close();
    return new TransactionResponseBuilder().createTransactionResponse();
  }

  public TransactionResponse delete(HttpDelete deleteRequest) {
    CloseableHttpClient httpClient = getClosableHttpClient();
    printRequest(deleteRequest);

    TransactionResponseBuilder transactionResponse = new TransactionResponseBuilder();
    transactionResponse.setMethod(DELETE);
    transactionResponse.setEndpoint(deleteRequest.getURI().toString());

    HttpResponse response = null;
    try {
      response = httpClient.execute(deleteRequest);
      retryIfNeeded(response, "DELETE");
      transactionResponse.setStatusCode(getStatusCode(response));
      transactionResponse.setResponseBody(getResponseText(response));
      transactionResponse.setSuccess(true);
    } catch (IOException e) {
      transactionResponse.setStatusCode(-1);
      transactionResponse.setFailureReason("IOException");
    } catch (AuthenticationException e) {
      transactionResponse.setStatusCode(response.getStatusLine().getStatusCode());
      transactionResponse.setFailureReason(response.getStatusLine().getReasonPhrase());
    }

    try {
      httpClient.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return transactionResponse.createTransactionResponse();
  }

  public TransactionResponse patch(HttpPatch patchRequest) throws IOException, AuthenticationException {
    CloseableHttpClient httpClient = getClosableHttpClient();
    HttpResponse response = httpClient.execute(patchRequest);
    retryIfNeeded(response, "PATCH");
    String responseText = getResponseText(response);
    httpClient.close();
    return new TransactionResponseBuilder().createTransactionResponse();
  }

  private void retryIfNeeded(HttpResponse response, String httpMethod) throws AuthenticationException {
    if (response.getStatusLine().getStatusCode() == 401) {
      throw new AuthenticationException(UNAUTHORIZED, "401 Unauthorized during POST");
    }

    if (!acceptableResponseCodes.get(httpMethod).contains(response.getStatusLine().getStatusCode())) {
      throw new RuntimeException("Request failed : HTTP error code " + response.getStatusLine().getStatusCode() + " "+response.getStatusLine().getReasonPhrase());
    }
  }

  private int getStatusCode(HttpResponse response) {
    return response.getStatusLine().getStatusCode();
  }

  private String getResponseText(HttpResponse response) throws IOException {
    if (response != null && !response.equals("")) {
      return IOUtils.toString(
          response.getEntity().getContent(),
          UTF_8.name());
    }
    return "";
  }

  private CloseableHttpClient getClosableHttpClient() {
    return HttpClients.createDefault();
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
