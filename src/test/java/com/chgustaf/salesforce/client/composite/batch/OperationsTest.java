package com.chgustaf.salesforce.client.composite.batch;

import static com.chgustaf.salesforce.client.TestUtils.getCompositeBatchTransaction;
import static com.chgustaf.salesforce.client.composite.batch.Operations.get;
import static com.chgustaf.salesforce.client.composite.batch.Operations.query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Primary_Test_Object__c;
import com.chgustaf.salesforce.client.composite.domain.Query;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OperationsTest {

  @Test
  void get_success() throws TransactionException, IOException, AuthenticationException {
    String id = "0013V000009ikVtQAI";
    String name = "PTO-1609674290821";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    String responseJson =
        "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":\n"
        + "    {\"attributes\":{\"type\":\"Primary_Test_Object__c\","
        + "\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/0013V000009ikVtQAI\"},"
        + "\"Id\":\"0013V000009ikVtQAI\",\"Name\":\""+name+"\"}}]}\n";
    when(salesforceCompositeBatchClient.compositeBatchCall(any())).thenReturn(responseJson);
    CompositeBatchTransaction compositeBatchTransaction = getCompositeBatchTransaction(responseJson);
    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setId(id);
    String referenceId = testObject.getReferenceId();
    testObject = get(testObject, salesforceCompositeBatchClient);
    assertNotNull(testObject);
    assertNotEquals(testObject.getReferenceId(), referenceId);
    assertEquals(testObject.getTestName(), name);
  }

  @Test
  void get_fail() {
    //TODO: Continue write tests for the Operations class
  }

  @Test
  void query_sucess() throws TransactionException, IOException, AuthenticationException {
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    String httpPostReturnValue = "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":\n"
                                 + "{\"totalSize\":13,\"done\":true,"
                                 + "\"records\":[{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000008DqqoQAC\"},\"Id\":\"5003V000008DqqoQAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V5w5QAC\"},\"Id\":\"5003V000009V5w5QAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6IOQA0\"},\"Id\":\"5003V000009V6IOQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6IPQA0\"},\"Id\":\"5003V000009V6IPQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V689QAC\"},\"Id\":\"5003V000009V689QAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V68AQAS\"},\"Id\":\"5003V000009V68AQAS\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6MQQA0\"},\"Id\":\"5003V000009V6MQQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6MRQA0\"},\"Id\":\"5003V000009V6MRQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6N9QAK\"},\"Id\":\"5003V000009V6N9QAK\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NAQA0\"},\"Id\":\"5003V000009V6NAQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NOQA0\"},\"Id\":\"5003V000009V6NOQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NPQA0\"},\"Id\":\"5003V000009V6NPQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V5w6QAC\"},\"Id\":\"5003V000009V5w6QAC\"}]}}]}";

    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(httpPostReturnValue);

    Query<Primary_Test_Object__c> query = new Query<>(Primary_Test_Object__c.class);
    query.setQuery("SELECT Id FROM Primary_Test_Object__c");
    List<Primary_Test_Object__c> resultList = query(query, salesforceCompositeBatchClient);
    assertEquals(13, resultList.size());
  }

  @Test
  void query_invalidFieldError() throws TransactionException, IOException, AuthenticationException {
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    String httpPostReturnValue =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"INVALID_FIELD\","
        + "\"message\":\"\\nSELECT Subjec FROM Primary_Test_Object__c\\n       ^\\nERROR at "
        + "Row:1:Column:8\\nNo such column &#39;Subjec&#39; on entity &#39;Case&#39;. If you are attempting to use a custom field, be sure to append the &#39;__c&#39; after the custom field name. Please reference your WSDL or the describe call for the appropriate names.\"}],\"statusCode\":400}]}\n";

    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(httpPostReturnValue);

    Query<Primary_Test_Object__c> query = new Query<>(Primary_Test_Object__c.class);
    query.setQuery("SELECT Subjec FROM Primary_Test_Object__c");
    TransactionException exception = assertThrows(TransactionException.class, () -> query(query,
        salesforceCompositeBatchClient));
    assertTrue(exception.getMessage().contains("INVALID_FIELD"));
  }

  @Test
  void query_multiPageResultsSet() {

  }
}
