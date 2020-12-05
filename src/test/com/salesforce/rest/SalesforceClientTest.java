package test.com.salesforce.rest;

import static com.salesforce.rest.SalesforceClient.AuthenticationFlow.JWT;
import static com.salesforce.rest.SalesforceClient.AuthenticationFlow.USER_PASSWORD;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.salesforce.authentication.secrets.Secrets;
import com.salesforce.authentication.secrets.SecretsUtil;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.BaseHTTPClient;
import com.salesforce.rest.SalesforceClient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

public class SalesforceClientTest {

  @Test
  public void testAuthenticateJWT_getAccessToken()
      throws IOException, AuthenticationException{

    BaseHTTPClient baseHTTPClient = mock(BaseHTTPClient.class);
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);

    Secrets secrets = SecretsUtil.readTestCredentials("secrets.json");
    SalesforceClient client = new SalesforceClient(JWT, baseHTTPClient, secrets);
    assertNotNull(client.accessParameters.accessToken);
  }

  @Test
  public void testAuthenticateUserPassword_getAccessToken()
      throws IOException, AuthenticationException {

    BaseHTTPClient baseHTTPClient = mock(BaseHTTPClient.class);
    String response = readResourceJSON("username-password-authenticate-success.json");
    when(baseHTTPClient.post(any(HttpPost.class))).thenReturn(response);

    Secrets secrets = SecretsUtil.readTestCredentials("secrets.json");
    SalesforceClient client = new SalesforceClient(USER_PASSWORD, baseHTTPClient, secrets);
  }

  public static String readResourceJSON(String fileName) throws IOException {
    File file = new File("src/test/resources/"+fileName);
    String json = Files.readString(file.toPath());
    return json;
  }
}
