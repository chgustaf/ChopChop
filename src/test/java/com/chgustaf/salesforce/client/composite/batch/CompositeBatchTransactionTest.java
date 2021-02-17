package com.chgustaf.salesforce.client.composite.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Primary_Test_Object__c;
import com.chgustaf.salesforce.client.composite.domain.Query;
import com.chgustaf.salesforce.client.composite.domain.TransactionError;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;


public class CompositeBatchTransactionTest {

  @Test
  public void deserializeToCompositeBatchResponse_success() throws JsonProcessingException {
    String responseJson =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"UNKNOWN_EXCEPTION\",\"message\":\"An unexpected error occurred. Please include this ErrorId if you contact support: 8425053-14906 (608530858)\"}],\"statusCode\":500}]}\n";
    ObjectMapper mapper = new ObjectMapper();
    CompositeBatchResponse compositeBatchResponse =
        mapper.readValue(responseJson, CompositeBatchResponse.class);
    assertTrue(compositeBatchResponse.getHasErrors());
    assertEquals(1, compositeBatchResponse.getResults().length);
    assertNotNull(compositeBatchResponse.getResults()[0].getResult());
  }

  @Test
  public void batchPost_success() throws IOException, AuthenticationException {
    String id = "0013V000009id1YQAQ";
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":201,\"result\":\n"
                          + "{\"id\":\""+id+"\",\"success\":true,\"errors\":[]}}]}";

    CompositeBatchTransaction compositeBatchTransaction =
        getCompositeBatchTransaction(responseJson);

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setTestNumber(1);
    compositeBatchTransaction.create(testObject);

    assertTrue(compositeBatchTransaction.execute());

    testObject = compositeBatchTransaction.getRecord(testObject.getReferenceId(), testObject.getClass());
    assertEquals(id, testObject.getId());
    assertTrue(1 == testObject.getTestNumber());
    assertTrue(testObject.getSuccess());
  }

  @Test
  public void batchPost_unknownException() throws IOException, AuthenticationException {
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"UNKNOWN_EXCEPTION\",\"message\":\"An unexpected error occurred. Please include this ErrorId if you contact support: 8425053-14906 (608530858)\"}],\"statusCode\":500}]}";

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setTestNumber(1);

    SalesforceCompositeBatchClient salesforceCompositeBatchClient = mockCompositeBatchResponse(responseJson);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);
    compositeBatchTransaction.create(testObject);
    assertFalse(compositeBatchTransaction.execute());

    testObject = compositeBatchTransaction.getRecord(testObject.getReferenceId(), testObject.getClass());
    assertFalse(testObject.getSuccess());
    assertEquals(1, testObject.getErrors().size());
    assertEquals("UNKNOWN_EXCEPTION", ((TransactionError)testObject.getErrors().get(0)).getErrorCode());
    assertTrue(((TransactionError) testObject.getErrors().get(0)).getMessage().contains("An unexpected error occurred"));
  }

  @Test
  public void batchPost_invalidFieldException() throws IOException, AuthenticationException {
    String responseJson =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"INVALID_FIELD\",\"message\":\"No such column &#39;aFieldThatDoesNotExist&#39; on sobject of type Account\"}],\"statusCode\":400}]}";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mockCompositeBatchResponse(responseJson);

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setTestNumber(1);

    CompositeBatchTransaction compositeBatchTransaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    compositeBatchTransaction.create(testObject);
    Boolean success = compositeBatchTransaction.execute();
    assertFalse(success);

    testObject = compositeBatchTransaction.getRecord(testObject.getReferenceId(),
        Primary_Test_Object__c.class);

    assertFalse(testObject.getSuccess());
    assertEquals(1, testObject.getErrors().size());
    assertEquals(
        "INVALID_FIELD",
        ((TransactionError) testObject.getErrors().get(0)).getErrorCode());
    assertTrue(
        ((TransactionError) testObject.getErrors().get(0))
            .getMessage()
            .contains("No such column"));
  }

  @Test
  public void batchDelete_success() throws IOException, AuthenticationException {
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":204,"
                          + "\"result\":null}]}";
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setTestNumber(1);

    assertNotEquals(null, testObject.getAttributes());
    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);
    compositeBatchTransaction.delete(testObject);
    boolean success = compositeBatchTransaction.execute();

    assertTrue(success);

    testObject = compositeBatchTransaction.getRecord(testObject.getReferenceId(), testObject.getClass());
    assertEquals(true, testObject.getSuccess());
  }

  @Test
  public void batchDelete_FailChildRecords() throws IOException, AuthenticationException {
    String id = "123456788";
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"DELETE_FAILED\",\"message\":\"Your attempt to delete Edge Communications could not be completed because some opportunities in that account were closed won. The opportunities that could not be deleted are shown below.: Edge Emergency Generator, Edge Installation, Edge SLA\\n\"}],\"statusCode\":400}]}\n";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mockCompositeBatchResponse(responseJson);
    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setId(id);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);
    compositeBatchTransaction.get(testObject);
    boolean success = compositeBatchTransaction.execute();
    assertFalse(success);
    testObject = compositeBatchTransaction.getRecord(testObject.getReferenceId(),
        testObject.getClass());

    assertFalse(testObject.getErrors().isEmpty());
    assertEquals(
        "DELETE_FAILED",
        ((TransactionError) testObject.getErrors().get(0)).getErrorCode());
  }

  @Test
  public void batchGet_success() throws IOException, AuthenticationException {
    String id = "0013V000009ikVtQAI";
    String name = "PTO-1609674290821";
    String responseJson =
        "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":\n"
            + "    {\"attributes\":{\"type\":\"Primary_Test_Object__c\","
        + "\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/0013V000009ikVtQAI\"},"
        + "\"Id\":\"0013V000009ikVtQAI\",\"Name\":\"PTO-1609674290821\"}}]}\n"
            + "    ";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
    mockCompositeBatchResponse(responseJson);

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setId(id);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);
    compositeBatchTransaction.get(testObject);
    boolean success = compositeBatchTransaction.execute();

    testObject = compositeBatchTransaction.getRecord(testObject.getReferenceId(), testObject.getClass());

    assertTrue(success);
    assertEquals(name, testObject.getTestName());
  }

  @Test
  public void batchGet_notFound() throws IOException, AuthenticationException {
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"NOT_FOUND\",\"message\":\"Provided external ID field does not exist or is not accessible: null\"}],\"statusCode\":404}]}";
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setTestNumber(1);
    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);
    compositeBatchTransaction.get(testObject);
    boolean success = compositeBatchTransaction.execute();

    testObject = compositeBatchTransaction.getRecord(testObject.getReferenceId(),
        testObject.getClass());
    assertFalse(success);
  }

  @Test
  public void batchUpdate_success() throws IOException, AuthenticationException {
    String id = "123456789";
    int testNumber = 1;
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":204,"
                          + "\"result\":null}]}";
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setId(id);
    testObject.setTestNumber(testNumber);
    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);
    compositeBatchTransaction.update(testObject);
    boolean success = compositeBatchTransaction.execute();
    assertTrue(success);

    testObject = compositeBatchTransaction.getRecord(testObject.getReferenceId(),
        testObject.getClass());
    assertTrue(testObject.getSuccess());
    assertEquals(id, testObject.getId());
    assertEquals(Integer.valueOf(testNumber), testObject.getTestNumber());
  }


  @Test
  public void query_success() throws IOException, AuthenticationException {
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":\n"
                          + "{\"totalSize\":13,\"done\":true,"
                          + "\"records\":[{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000008DqqoQAC\"},\"Id\":\"5003V000008DqqoQAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V5w5QAC\"},\"Id\":\"5003V000009V5w5QAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6IOQA0\"},\"Id\":\"5003V000009V6IOQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6IPQA0\"},\"Id\":\"5003V000009V6IPQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V689QAC\"},\"Id\":\"5003V000009V689QAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V68AQAS\"},\"Id\":\"5003V000009V68AQAS\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6MQQA0\"},\"Id\":\"5003V000009V6MQQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6MRQA0\"},\"Id\":\"5003V000009V6MRQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6N9QAK\"},\"Id\":\"5003V000009V6N9QAK\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NAQA0\"},\"Id\":\"5003V000009V6NAQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NOQA0\"},\"Id\":\"5003V000009V6NOQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NPQA0\"},\"Id\":\"5003V000009V6NPQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V5w6QAC\"},\"Id\":\"5003V000009V5w6QAC\"}]}}]}";
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);
    String queryString = "SELECT id FROM Primary_Test_Object__c";
    Query<Primary_Test_Object__c> query = new Query<>(queryString, Primary_Test_Object__c.class);
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    transaction.query(query);
    transaction.execute();
    List<Primary_Test_Object__c> queryResult =
        transaction.getQueryResult(query.getReferenceId(),
        query.getEntityClass());
    assertEquals(13, queryResult.size());
  }

  @Test
  public void query_nextRecordsUrl() throws IOException, AuthenticationException {
    String responseJson =
        "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":{\"totalSize\":13,"
        + "\"done\":false,\"nextRecordsUrl\":\"/services/data/v50.0/query/01g09000000h1LvAAI-2000\",\"records\":[{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000008DqqoQAC\"},\"Id\":\"5003V000008DqqoQAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V5w5QAC\"},\"Id\":\"5003V000009V5w5QAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6IOQA0\"},\"Id\":\"5003V000009V6IOQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6IPQA0\"},\"Id\":\"5003V000009V6IPQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V689QAC\"},\"Id\":\"5003V000009V689QAC\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V68AQAS\"},\"Id\":\"5003V000009V68AQAS\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6MQQA0\"},\"Id\":\"5003V000009V6MQQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6MRQA0\"},\"Id\":\"5003V000009V6MRQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6N9QAK\"},\"Id\":\"5003V000009V6N9QAK\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NAQA0\"},\"Id\":\"5003V000009V6NAQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NOQA0\"},\"Id\":\"5003V000009V6NOQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V6NPQA0\"},\"Id\":\"5003V000009V6NPQA0\"},{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/5003V000009V5w6QAC\"},\"Id\":\"5003V000009V5w6QAC\"}]}}]}";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);
    String queryString = "SELECT id FROM Primary_Test_Object__c";
    Query<Primary_Test_Object__c> query = new Query<>(queryString, Primary_Test_Object__c.class);
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    transaction.query(query);
    transaction.execute();
    List<Primary_Test_Object__c> queryResult =
        transaction.getQueryResult(query.getReferenceId(),
            query.getEntityClass());
    assertEquals(13, queryResult.size());
  }

  @Test
  public void query_invalidField() throws IOException, AuthenticationException {
    String invalidFieldError =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"INVALID_FIELD\","
        + "\"message\":\"\\nSELECT Subjec FROM Primary_Test_Object__c\\n       ^\\nERROR at "
        + "Row:1:Column:8\\nNo such column &#39;Subjec&#39; on entity &#39;Case&#39;. If you are attempting to use a custom field, be sure to append the &#39;__c&#39; after the custom field name. Please reference your WSDL or the describe call for the appropriate names.\"}],\"statusCode\":400}]}\n";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(invalidFieldError);
    String queryString = "SELECT subjec FROM Primary_Test_Object__c";
    Query<Primary_Test_Object__c> query = new Query<>(queryString, Primary_Test_Object__c.class);
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    transaction.query(query);
    transaction.execute();
    List<Primary_Test_Object__c> queryResult =
        transaction.getQueryResult(query.getReferenceId(),
            query.getEntityClass());
    assertEquals(13, queryResult.size());

  }


  @Test
  public void parse_Errors() throws IOException, AuthenticationException {


  }

  @Test
  public void query_withNextRecordsUrl() {

  }


  private SalesforceCompositeBatchClient mockCompositeBatchResponse(String json) throws IOException, AuthenticationException {
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(json);
    return salesforceCompositeBatchClient;
  }

  private CompositeBatchTransaction getCompositeBatchTransaction(String responseJson)
      throws IOException, AuthenticationException {
    SalesforceCompositeBatchClient salesforceCompositeBatchClient = mockCompositeBatchResponse(responseJson);
    return new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
  }
}