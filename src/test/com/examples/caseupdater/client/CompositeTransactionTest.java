package com.examples.caseupdater.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.examples.caseupdater.client.composite.Body;
import com.examples.caseupdater.client.composite.CompositeResponse;
import com.examples.caseupdater.client.composite.Response;
import com.examples.caseupdater.client.domain.Account;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.ImprovedSalesforceClient;
import java.io.IOException;
import org.junit.Test;

public class CompositeTransactionTest {

  @Test
  public void execute() throws IOException, AuthenticationException {
    String responseJson = "{\"compositeResponse\":[{\"body\":[{\"message\":\"Account Name: data "
                        + "value "
                      + "too large: eajYGvBJUFwSVmIGHtEznoT4iZHxXEzBzvNKKkNPDvSfJPuo1rB32Dh6oeaJhDSSSlxZgXGdwft6dvCkvzB25VUxU7DsgwfAA9rkF2fJUx944hjmHyKTVNxeSFSIptvDUdf2R4dkyF855wa8FVa9mX7sMxAd1VrxMKQLSELGv47MfnVQoKTuKtHLoGR8U8YqEB8K2mQtZh15U8MB9nQ5CIs3gv1Eb2crm0sENtrGwQtbzchx1Ir3ZOCFvVl3PGRU (max length=255)\",\"errorCode\":\"STRING_TOO_LONG\",\"fields\":[\"Name\"]}],\"httpHeaders\":{},\"httpStatusCode\":400,\"referenceId\":\"70b35cde-2eed-4ff8-a21a-ca7c5a751c30\"}]}\r\n";

    ImprovedSalesforceClient improvedSalesforceClient =
        mock(ImprovedSalesforceClient.class);
    when(improvedSalesforceClient.compositeCall(any(String.class))).thenReturn(responseJson);

    CompositeTransaction compositeTransaction = new CompositeTransaction(improvedSalesforceClient);
    compositeTransaction.create(new Account("Example Account"));
    Response response = compositeTransaction.execute();

    CompositeResponse[] compositeResponses = response.compositeResponse;
    assertEquals(1, compositeResponses.length);
    CompositeResponse compositeResponse = compositeResponses[0];
    assertEquals(400, compositeResponse.httpStatusCode);
    assertEquals("70b35cde-2eed-4ff8-a21a-ca7c5a751c30", compositeResponse.referenceId);

    //assertEquals(1, compositeResponse.body.length);
    Body body = compositeResponse.body;
    assertEquals(1, body.fields.length);
    assertNull(body.id);
    assertEquals(false, body.success);
    assertNull(body.errors);
    assertEquals("STRING_TOO_LONG", body.errorCode);
    assertEquals("Account Name: data value too large: "
                 +
                 "eajYGvBJUFwSVmIGHtEznoT4iZHxXEzBzvNKKkNPDvSfJPuo1rB32Dh6oeaJhDSSSlxZgXGdwft6dvCkvzB25VUxU7DsgwfAA9rkF2fJUx944hjmHyKTVNxeSFSIptvDUdf2R4dkyF855wa8FVa9mX7sMxAd1VrxMKQLSELGv47MfnVQoKTuKtHLoGR8U8YqEB8K2mQtZh15U8MB9nQ5CIs3gv1Eb2crm0sENtrGwQtbzchx1Ir3ZOCFvVl3PGRU (max length=255)", body.message);

  }

  @Test
  public void compositePostRequest_Success() throws IOException, AuthenticationException {
    String responseJson = "{\"compositeResponse\":[{\"body\":{\"id\":\"0013V000009ic0sQAA\","
                          + "\"success\":true,\"errors\":[]},\"httpHeaders\":{\"Location\":\"/services/data/v50.0/sobjects/Account/0013V000009ic0sQAA\"},\"httpStatusCode\":201,\"referenceId\":\"070b0f39-41d0-4b63-a0af-b8eff378086e\"}]}\n";
    ImprovedSalesforceClient improvedSalesforceClient = mock(ImprovedSalesforceClient.class);
    when(improvedSalesforceClient.compositeCall(any(String.class))).thenReturn(responseJson);

    CompositeTransaction compositeTransaction = new CompositeTransaction(improvedSalesforceClient);
    compositeTransaction.create(new Account("Example Account 2"));
    Response response = compositeTransaction.execute();
    //assertEquals(1, composite);
  }

}