package com.examples.caseupdater.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.examples.caseupdater.client.composite.batch.CompositeBatchResponse;
import com.examples.caseupdater.client.domain.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.ImprovedSalesforceClient;
import java.io.IOException;
import org.junit.Test;

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
    ImprovedSalesforceClient improvedSalesforceClient = mock(ImprovedSalesforceClient.class);
    when(improvedSalesforceClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(improvedSalesforceClient);
    compositeBatchTransaction.create(new Account("Example Account"));
    //CompositeBatchResponse response = compositeBatchTransaction.execute();
  }

  @Test
  public void batchPost_invalidFieldException() throws IOException, AuthenticationException {
    String responseJson =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"INVALID_FIELD\",\"message\":\"No such column &#39;aFieldThatDoesNotExist&#39; on sobject of type Account\"}],\"statusCode\":400}]}";
    ImprovedSalesforceClient improvedSalesforceClient = mock(ImprovedSalesforceClient.class);
    when(improvedSalesforceClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    Account account = new Account("Example Account");
    String uuid1 = account.getReferenceId();

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(improvedSalesforceClient);
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
    ImprovedSalesforceClient improvedSalesforceClient = mock(ImprovedSalesforceClient.class);
    when(improvedSalesforceClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    Account account = new Account(accountName);
    String uuid1 = account.getReferenceId();

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(improvedSalesforceClient);
    compositeBatchTransaction.create(account);
    Boolean success = compositeBatchTransaction.execute();

    assertTrue(success);

    account = compositeBatchTransaction.getRecord(account.getReferenceId(), account.getClass());
    assertEquals(id, account.getId());
    assertEquals(accountName, account.getName());
    assertTrue(account.getSuccess());

    // TODO Continue writing test methods
  }

  @Test
  public void batchDelete_success() throws IOException, AuthenticationException {
    String id = "123456789";
    String accountName = "";
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":204,"
                          + "\"result\":null}]}";
    ImprovedSalesforceClient improvedSalesforceClient = mock(ImprovedSalesforceClient.class);
    when(improvedSalesforceClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    Account account = new Account();
    account.setId(id);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(improvedSalesforceClient);
    compositeBatchTransaction.delete(account);
    boolean success = compositeBatchTransaction.execute();

    assertTrue(success);

    account = compositeBatchTransaction.getRecord(account.getReferenceId(), account.getClass());
    assertEquals(id, account.getId());
    assertEquals(true, account.getSuccess());
  }

  @Test
  public void batchGet_success() throws IOException, AuthenticationException {
    String id = "0013V000009ikVtQAI";
    String responseJson =
        "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":\n"
            + "    {\"attributes\":{\"type\":\"Account\",\"url\":\"/services/data/v50.0/sobjects/Account/0013V000009ikVtQAI\"},\"Id\":\"0013V000009ikVtQAI\",\"Name\":\"Test Account 1609674290821\"}}]}\n"
            + "    ";
    ImprovedSalesforceClient improvedSalesforceClient = mock(ImprovedSalesforceClient.class);
    when(improvedSalesforceClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    Account account = new Account();
    account.setId(id);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(improvedSalesforceClient);
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
}