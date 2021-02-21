package com.chgustaf.salesforce.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chgustaf.salesforce.authentication.AccessParameters;
import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.client.composite.batch.CompositeBatchTransaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestUtils {

  private static SalesforceCompositeBatchClient mockSalesforceCompositeBatchClient(
      final String responseJson) throws IOException, AuthenticationException, TransactionException {
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class)))
        .thenReturn(responseJson);
    return salesforceCompositeBatchClient;
  }

  public static CompositeBatchTransaction getCompositeBatchTransaction(String responseJson)
      throws IOException, AuthenticationException, TransactionException {
      SalesforceCompositeBatchClient salesforceCompositeBatchClient = mockSalesforceCompositeBatchClient(responseJson);
      return new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
  }

  public static String readResourceJSON(String fileName) throws IOException {
    File file = new File("src/test/resources/" + fileName);
    String json = Files.readString(file.toPath());
    return json;
  }

  public static AccessParameters getAccessParameters() throws IOException {
    String response = readResourceJSON("username-password-authenticate-success.json");
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(response, AccessParameters.class);
  }
}
