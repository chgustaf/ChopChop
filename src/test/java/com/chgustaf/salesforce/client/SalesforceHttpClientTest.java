package com.chgustaf.salesforce.client;

import static com.chgustaf.salesforce.authentication.exceptions.AuthenticationException.Code.UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.secrets.Secrets;
import com.chgustaf.salesforce.authentication.secrets.SecretsUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SalesforceHttpClientTest {

  BaseHTTPClient baseHTTPClient;

  @BeforeEach
  void init() {
    baseHTTPClient = mock(BaseHTTPClient.class);
  }

  @Test
  public void incorrectAuthenticationFlow_fail()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);
    Secrets secrets = SecretsUtil
        .readTestCredentials("secrets_incorrect_authentication_method.json");
    AuthenticationException authenticationException = assertThrows(AuthenticationException.class,
        () -> new SalesforceHttpClient(baseHTTPClient,
      secrets));
    assertTrue(authenticationException.getMessage().contains("Unknown authentication flow:"));
  }

  @Test
  public void initClassNoAuthenitcationFlow_fail()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);
    Secrets secrets = SecretsUtil
        .readTestCredentials("secrets_incorrect_authentication_method.json");
    AuthenticationException authenticationException = assertThrows(AuthenticationException.class,
        () -> new SalesforceHttpClient(baseHTTPClient,
            secrets));
    assertTrue(authenticationException.getMessage().contains("Unknown authentication flow:"));
  }

  @Test
  public void testAuthenticateUserPasswordGetAccessToken_success()
      throws IOException, AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);
    Secrets secrets = SecretsUtil.readTestCredentials("secrets_usernamepassword.json");
    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);

    assertEquals(SalesforceHttpClient.AuthenticationFlow.USER_PASSWORD,
        client.getAuthenticationFlow(secrets));
    assertNotNull(client.getAccessParameters().getAccessToken());
  }

  @Test
  public void testAuthenticateJWTGetAccessToken_success()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);
    Secrets secrets = SecretsUtil
        .readTestCredentials("secrets_jwt.json");
    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);

    assertEquals(SalesforceHttpClient.AuthenticationFlow.JWT,
        client.getAuthenticationFlow(secrets));
    assertNotNull(client.getAccessParameters().getAccessToken());
  }

  @Test
  public void testExecuteHttpRequest_POST()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);
    Secrets secrets = SecretsUtil
        .readTestCredentials("secrets_jwt.json");
    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);
    HttpPost post = new HttpPost();
    client.executeHttpRequest(post);
  }

  @Test
  public void testExecuteHttpRequest_GET()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);
    when(baseHTTPClient.get(any(HttpGet.class))).thenReturn(response);
    Secrets secrets = SecretsUtil
        .readTestCredentials("secrets_jwt.json");
    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);
    HttpGet get = new HttpGet();
    client.executeHttpRequest(get);
  }

  @Test
  public void testExecuteHttpRequest_PATCH()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);
    when(baseHTTPClient.patch(any(HttpPatch.class))).thenReturn(response);
    Secrets secrets = SecretsUtil
        .readTestCredentials("secrets_jwt.json");
    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);
    HttpPatch patch = new HttpPatch();
    client.executeHttpRequest(patch);
  }

  @Test
  public void testExecuteHttpRequest_DELETE()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);
    when(baseHTTPClient.delete(any(HttpDelete.class))).thenReturn(response);
    Secrets secrets = SecretsUtil
        .readTestCredentials("secrets_jwt.json");
    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);
    HttpDelete delete = new HttpDelete();
    client.executeHttpRequest(delete);
  }

  @Test
  public void test401onAuthentication_POST()
      throws IOException,
             AuthenticationException {
    when(baseHTTPClient.post(any(HttpPost.class))).thenThrow(new AuthenticationException(UNAUTHORIZED, "401 Unauthorized during POST"));
    Secrets secrets = SecretsUtil.readTestCredentials("secrets_jwt.json");
    AuthenticationException authenticationException = assertThrows(AuthenticationException.class,
        () -> new SalesforceHttpClient(baseHTTPClient,
        secrets));

    assertEquals(UNAUTHORIZED, authenticationException.getCode());
    assertEquals("401 Unauthorized during POST", authenticationException.getMessage());
  }

  @Test
  public void test401onSecondPostCallFailedReauthentication_POST()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response).thenThrow(new AuthenticationException(UNAUTHORIZED, "401 Unauthorized during POST"));
    Secrets secrets = SecretsUtil.readTestCredentials("secrets_jwt.json");


    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);
    HttpPost post = new HttpPost();
    AuthenticationException authenticationException = assertThrows(AuthenticationException.class,
        () -> client.executeHttpRequest(post));


    assertEquals(UNAUTHORIZED, authenticationException.getCode());
    assertEquals("401 Unauthorized during POST", authenticationException.getMessage());
  }

  @Test
  public void test401onSecondPostCallButReauthenticationSuccessful_POST()
      throws IOException,
             AuthenticationException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response)
        .thenThrow(new AuthenticationException(UNAUTHORIZED, "401 Unauthorized during POST"))
        .thenReturn(response);
    Secrets secrets = SecretsUtil.readTestCredentials("secrets_jwt.json");

    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);
    HttpPost post = new HttpPost();
    client.executeHttpRequest(post);

    assertNotNull(client.getAccessParameters().getAccessToken());
  }

  @Test
  public void executeHttpRequestThrowsError() {

  }

  public static String readResourceJSON(String fileName) throws IOException {
    File file = new File("src/test/resources/" + fileName);
    String json = Files.readString(file.toPath());
    return json;
  }
}
