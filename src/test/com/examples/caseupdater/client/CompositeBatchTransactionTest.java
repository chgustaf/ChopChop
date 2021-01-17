package com.examples.caseupdater.client;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.examples.caseupdater.client.domain.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.authentication.exceptions.AuthenticationException;
import com.salesforce.client.SalesforceCompositeBatchClient;
import com.salesforce.client.composite.batch.CompositeBatchTransaction;
import com.salesforce.client.composite.dto.CompositeBatchResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;


public class CompositeBatchTransactionTest {


  @Test
  public void deserialize() throws JsonProcessingException {
    String responseJson =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"UNKNOWN_EXCEPTION\",\"message\":\"An unexpected error occurred. Please include this ErrorId if you contact support: 8425053-14906 (608530858)\"}],\"statusCode\":500}]}\n";
    ObjectMapper mapper = new ObjectMapper();
    mapper.readValue(responseJson, CompositeBatchResponse.class);
  }

  @Test
  public void batchPost_unknownException() throws IOException, AuthenticationException {
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"UNKNOWN_EXCEPTION\",\"message\":\"An unexpected error occurred. Please include this ErrorId if you contact support: 8425053-14906 (608530858)\"}],\"statusCode\":500}]}";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient = mockCompositeBatchResponse(responseJson);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    compositeBatchTransaction.create(new Account("Example Account"));
    //CompositeBatchResponse response = compositeBatchTransaction.execute();
  }

  @Test
  public void batchPost_invalidFieldException() throws IOException, AuthenticationException {
    String responseJson =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"INVALID_FIELD\",\"message\":\"No such column &#39;aFieldThatDoesNotExist&#39; on sobject of type Account\"}],\"statusCode\":400}]}";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient = mockCompositeBatchResponse(responseJson);

    Account account = new Account("Example Account");
    String uuid1 = account.getReferenceId();

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    compositeBatchTransaction.create(account);
    Boolean success = compositeBatchTransaction.execute();

    account = compositeBatchTransaction.getRecord(account.getReferenceId(), account.getClass());
    String uuid2 = account.getReferenceId();

    //assertEquals(400, account.getStatusCode().intValue());
  }

  @Test
  public void batchPost_success() throws IOException, AuthenticationException {
    String id = "0013V000009id1YQAQ";
    String accountName = "Example Account 1";
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":201,\"result\":\n"
                          + "{\"id\":\""+id+"\",\"success\":true,\"errors\":[]}}]}";

    SalesforceCompositeBatchClient salesforceCompositeBatchClient = mockCompositeBatchResponse(responseJson);
    Account account = new Account(accountName);
    String uuid1 = account.getReferenceId();

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    compositeBatchTransaction.create(account);
    Boolean success = compositeBatchTransaction.execute();

    assertTrue(success);

    account = compositeBatchTransaction.getRecord(account.getReferenceId(), account.getClass());
    assertEquals(id, account.getId());
    assertEquals(accountName, account.getName());
    assertTrue(account.getSuccess());

    // TODO Add test methods to all known scenarios
  }

  @Test
  public void batchDelete_success() throws IOException, AuthenticationException {
    String id = "123456789";
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":204,"
                          + "\"result\":null}]}";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient = mockCompositeBatchResponse(responseJson);

    Account account = new Account();
    account.setId(id);
    account.setDescription("Some description");

    assertNotEquals(null, account.getAttributes());
    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    compositeBatchTransaction.delete(account);
    boolean success = compositeBatchTransaction.execute();

    assertTrue(success);

    account = compositeBatchTransaction.getRecord(account.getReferenceId(), account.getClass());
    assertEquals(id, account.getId());
    assertEquals(true, account.getSuccess());
  }

  @Test
  public void batchDelete_FailChildRecords() throws IOException, AuthenticationException {

    String id = "123456788";
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"DELETE_FAILED\",\"message\":\"Your attempt to delete Edge Communications could not be completed because some opportunities in that account were closed won. The opportunities that could not be deleted are shown below.: Edge Emergency Generator, Edge Installation, Edge SLA\\n\"}],\"statusCode\":400}]}\n";
    Account account = new Account();
    account.setId(id);
    mockCompositeBatchResponse(responseJson);

    //TODO: fix all test scenarios

  }

  @Test
  public void batchGet_success() throws IOException, AuthenticationException {
    String id = "0013V000009ikVtQAI";
    String responseJson =
        "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":\n"
            + "    {\"attributes\":{\"type\":\"Account\",\"url\":\"/services/data/v50.0/sobjects/Account/0013V000009ikVtQAI\"},\"Id\":\"0013V000009ikVtQAI\",\"Name\":\"Test Account 1609674290821\"}}]}\n"
            + "    ";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
    mockCompositeBatchResponse(responseJson);

    Account account = new Account();
    account.setId(id);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    compositeBatchTransaction.get(account);
    boolean success = compositeBatchTransaction.execute();

    assertTrue(success);

  }

  @Test
  public void batchUpdate_success() {
    String id = "123456789";
    String accountName = "";
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":204,"
                          + "\"result\":null}]}";


  }

  @Test
  public void batchGet_notFound() {
    String responseJSON = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"NOT_FOUND\",\"message\":\"Provided external ID field does not exist or is not accessible: null\"}],\"statusCode\":404}]}";

  }

  @Test
  public void query_success() {
    String responseJSON = "{\"totalSize\":13,\"done\":true,"
                          + "\"records\":[{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000008DqqoQAC\"},\"Id\":\"5003V000008DqqoQAC\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V5w5QAC\"},\"Id\":\"5003V000009V5w5QAC\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6IOQA0\"},\"Id\":\"5003V000009V6IOQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6IPQA0\"},\"Id\":\"5003V000009V6IPQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V689QAC\"},\"Id\":\"5003V000009V689QAC\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V68AQAS\"},\"Id\":\"5003V000009V68AQAS\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6MQQA0\"},\"Id\":\"5003V000009V6MQQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6MRQA0\"},\"Id\":\"5003V000009V6MRQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6N9QAK\"},\"Id\":\"5003V000009V6N9QAK\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6NAQA0\"},\"Id\":\"5003V000009V6NAQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6NOQA0\"},\"Id\":\"5003V000009V6NOQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6NPQA0\"},\"Id\":\"5003V000009V6NPQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V5w6QAC\"},\"Id\":\"5003V000009V5w6QAC\"}]}}]}\n";

  }

  @Test
  public void query_invalidField() {
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"INVALID_FIELD\",\"message\":\"\\nSELECT id, name, non_existing_field FROM Account\\n\\nERROR at Row:1:Column:18\\nNo such column &#39;non_existing_field&#39; on entity &#39;Account&#39;. If you are attempting to use a custom field, be sure to append the &#39;__c&#39; after the custom field name. Please reference your WSDL or the describe call for the appropriate names.\"}],\"statusCode\":400}]}\n";

  }


  private SalesforceCompositeBatchClient mockCompositeBatchResponse(String json) throws IOException, AuthenticationException {
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(json);
    return salesforceCompositeBatchClient;
  }
}