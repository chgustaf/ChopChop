package test.com.salesforce.rest;

import static com.salesforce.rest.SalesforceClient.AuthenticationFlow.JWT;
import static com.salesforce.rest.SalesforceClient.AuthenticationFlow.USER_PASSWORD;
import static com.salesforce.rest.TransactionResponse.Action.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.authentication.Authentication;
import com.salesforce.authentication.secrets.Secrets;
import com.salesforce.authentication.secrets.SecretsUtil;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.BaseHTTPClient;
import com.salesforce.rest.Response;
import com.salesforce.rest.SalesforceClient;
import com.salesforce.rest.TransactionResponse;
import com.salesforce.rest.TransactionResponseBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;

public class SalesforceClientTest {

  BaseHTTPClient baseHTTPClient;

  @BeforeEach
  public void setup() {
     baseHTTPClient = mock(BaseHTTPClient.class);
  }

  @Test
  public void testAuthenticateJWT_getAccessToken()
      throws IOException, AuthenticationException{
    setupJWTAuthentication();
    Secrets secrets = SecretsUtil.readTestCredentials("secrets.json");
    SalesforceClient client = new SalesforceClient(JWT, baseHTTPClient, secrets);
    assertNotNull(client.accessParameters.accessToken);
  }

  @Test
  public void testAuthenticateUserPassword_getAccessToken()
      throws IOException, AuthenticationException {
    setupUsernamePasswordAuthentication();
    Secrets secrets = SecretsUtil.readTestCredentials("secrets.json");
    SalesforceClient client = new SalesforceClient(USER_PASSWORD, baseHTTPClient, secrets);
  }

  public static String readResourceJSON(String fileName) throws IOException {
    File file = new File("src/test/resources/"+fileName);
    String json = Files.readString(file.toPath());
    return json;
  }

  @Test
  public void testPostMultipleRecords_success() throws IOException, AuthenticationException {

    setupJWTAuthentication();
    Secrets secrets = SecretsUtil.readTestCredentials("secrets.json");
    SalesforceClient client = new SalesforceClient(USER_PASSWORD, baseHTTPClient, secrets);

    String expectedPostUrl =
        client.accessParameters.instanceUrl +
        client.sobjectEndpoint +
        "Account";
    String objectName = "Account";
    String requestBody = "{\"name\": \"test\"}";
    String responseBody = "{\"id\":\"001D000000IqhCHGUS\",\"errors\" : [ ],\"success\" : true}";

    TransactionResponse mockedTransactionResponse =
        new TransactionResponseBuilder()
            .setMethod(POST)
            .setStatusCode(200)
            .setSuccess(true)
            .setEndpoint(expectedPostUrl)
            .setRequestBody(requestBody)
            .setResponseBody(responseBody)
            .createTransactionResponse();

    when(baseHTTPClient.post(Matchers.any(HttpPost.class))).thenReturn(mockedTransactionResponse);

    String responseString = client.postMultipleRecords(objectName, requestBody);
    Response response = new ObjectMapper().readValue(responseString, Response.class);
    assertEquals("POST", response.getAction());
    assertEquals("001D000000IqhCHGUS", response.getId());
    assertEquals(null, response.getError());
    assertEquals(201, response.getStatusCode());
  }

  private void setupJWTAuthentication() throws IOException, AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    TransactionResponse transactionResponse =
        new TransactionResponseBuilder()
            .setMethod(POST)
            .setSuccess(true)
            .setEndpoint(Authentication.LOGIN_URL)
            .setResponseBody(response)
            .createTransactionResponse();
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(transactionResponse);
  }

  private void setupUsernamePasswordAuthentication() throws IOException, AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    TransactionResponse transactionResponse =
        new TransactionResponseBuilder()
            .setMethod(POST)
            .setSuccess(true)
            .setEndpoint(Authentication.LOGIN_URL)
            .setResponseBody(response)
            .createTransactionResponse();
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(transactionResponse);
  }
}



