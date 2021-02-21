package com.chgustaf.salesforce.client;

import static com.chgustaf.salesforce.client.TestUtils.readResourceJSON;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.authentication.secrets.Secrets;
import com.chgustaf.salesforce.authentication.secrets.SecretsUtil;
import java.io.IOException;
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

}
