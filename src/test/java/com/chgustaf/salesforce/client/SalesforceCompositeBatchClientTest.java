package com.chgustaf.salesforce.client;

import static com.chgustaf.salesforce.client.TestUtils.getAccessParameters;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class SalesforceCompositeBatchClientTest {

  @Test
  void initClass_success() throws IOException, AuthenticationException, TransactionException {
    SalesforceHttpClient salesforceHttpClient = mock(SalesforceHttpClient.class);
    String httpPostRequestValue = "{\"Method\":\"POST\", \"\"}";
    String httpPostReturnValue = "Test Return Value";
    when(salesforceHttpClient.getAccessParameters()).thenReturn(getAccessParameters());
    when(salesforceHttpClient.executeHttpRequest(any())).thenReturn(httpPostReturnValue);

    SalesforceCompositeBatchClient batchClient = new SalesforceCompositeBatchClient(salesforceHttpClient);
    assertNotNull(batchClient.compositeBatchEndpoint);
    assertEquals(httpPostReturnValue, batchClient.compositeBatchCall(httpPostRequestValue));
  }

}
