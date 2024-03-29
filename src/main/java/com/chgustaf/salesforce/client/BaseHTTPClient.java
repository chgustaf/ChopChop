package com.chgustaf.salesforce.client;

import static com.chgustaf.salesforce.authentication.exceptions.AuthenticationException.Code.UNAUTHORIZED;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class BaseHTTPClient {

  public Map<String, Set<Integer>> acceptableResponseCodes;

  public BaseHTTPClient() {
    acceptableResponseCodes = new HashMap<>();
    acceptableResponseCodes.put("GET", new HashSet<>(Arrays.asList(200)));
    acceptableResponseCodes.put("POST", new HashSet<>(Arrays.asList(200, 201)));
    acceptableResponseCodes.put("PATCH", new HashSet<>(Arrays.asList(200)));
    acceptableResponseCodes.put("DELETE", new HashSet<>(Arrays.asList(200, 202, 204)));
  }

  public String post(HttpPost postRequest)
      throws IOException, AuthenticationException, TransactionException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(postRequest);
    checkResponse(response, "POST");
    String responseText = getResponseText(response);
    System.out.println("ResponseText " + responseText);
    httpClient.close();
    return responseText;
  }

  public String get(HttpGet getRequest)
      throws IOException, AuthenticationException, TransactionException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(getRequest);
    checkResponse(response, "GET");
    String responseText = getResponseText(response);
    httpClient.close();
    return responseText;
  }

  public String delete(HttpDelete deleteRequest)
      throws IOException, AuthenticationException, TransactionException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(deleteRequest);
    checkResponse(response, "DELETE");
    httpClient.close();
    return "";
  }

  public String patch(HttpPatch patchRequest)
      throws IOException, AuthenticationException, TransactionException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(patchRequest);
    checkResponse(response, "PATCH");
    String responseText = getResponseText(response);
    httpClient.close();
    return responseText ;
  }

  void checkResponse(HttpResponse response, String httpMethod)
      throws AuthenticationException, IOException, TransactionException {
    if (response.getStatusLine().getStatusCode() == 401) {
      throw new AuthenticationException(UNAUTHORIZED, "401 Unauthorized");
    }

    if (!acceptableResponseCodes.get(httpMethod).contains(response.getStatusLine().getStatusCode())) {
      throw new TransactionException("Request failed : HTTP error code " +
                                     response.getStatusLine().getStatusCode() + " " +getResponseText(response), response.getStatusLine().getReasonPhrase());
    }
  }

  String getResponseText(HttpResponse response) throws IOException {
    if (response != null) {
      return IOUtils.toString(
          response.getEntity().getContent(),
          UTF_8.name());
    }
    return "";
  }
}
