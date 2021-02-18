package com.chgustaf.salesforce.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.authentication.secrets.Secrets;
import com.chgustaf.salesforce.authentication.secrets.SecretsUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.Test;

public class SalesforceClientTest {

  @Test
  public void testAuthenticateJWT_getAccessToken()
      throws IOException,
             AuthenticationException, TransactionException {

    BaseHTTPClient baseHTTPClient = mock(BaseHTTPClient.class);
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);

    Secrets secrets = SecretsUtil
        .readTestCredentials("secrets_jwt.json");
    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);
    assertNotNull(client.getAccessParameters().getAccessToken());
  }

  @Test
  public void testAuthenticateUserPassword_getAccessToken()
      throws IOException, AuthenticationException, TransactionException {

    BaseHTTPClient baseHTTPClient = mock(BaseHTTPClient.class);
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);

    Secrets secrets = SecretsUtil.readTestCredentials("secrets_usernamepassword.json");

    SalesforceHttpClient client = new SalesforceHttpClient(baseHTTPClient, secrets);
  }

  public static String readResourceJSON(String fileName) throws IOException {
    File file = new File("src/test/resources/"+fileName);
    String json = Files.readString(file.toPath());
    return json;
  }
}
