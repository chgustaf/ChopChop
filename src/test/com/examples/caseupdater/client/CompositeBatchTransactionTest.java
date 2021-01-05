package com.examples.caseupdater.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.examples.caseupdater.client.composite.batch.CompositeBatchResponse;
import com.examples.caseupdater.client.domain.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.SalesforceCompositeBatchClient;
import java.io.IOException;
import java.util.List;
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
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

    CompositeBatchTransaction compositeBatchTransaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    compositeBatchTransaction.create(new Account("Example Account"));
    //CompositeBatchResponse response = compositeBatchTransaction.execute();
  }

  @Test
  public void batchPost_invalidFieldException() throws IOException, AuthenticationException {
    String responseJson =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"INVALID_FIELD\",\"message\":\"No such column &#39;aFieldThatDoesNotExist&#39; on sobject of type Account\"}],\"statusCode\":400}]}";
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

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
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

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
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

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
  public void batchDelete_FailChildRecords() {
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"DELETE_FAILED\",\"message\":\"Your attempt to delete Edge Communications could not be completed because some opportunities in that account were closed won. The opportunities that could not be deleted are shown below.: Edge Emergency Generator, Edge Installation, Edge SLA\\n\"}],\"statusCode\":400}]}\n";

  }

  @Test
  public void batchGet_success() throws IOException, AuthenticationException {
    String id = "0013V000009ikVtQAI";
    String responseJson =
        "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":\n"
            + "    {\"attributes\":{\"type\":\"Account\",\"url\":\"/services/data/v50.0/sobjects/Account/0013V000009ikVtQAI\"},\"Id\":\"0013V000009ikVtQAI\",\"Name\":\"Test Account 1609674290821\"}}]}\n"
            + "    ";
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(any(String.class))).thenReturn(responseJson);

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
  public void testSerialize() throws JsonProcessingException {
    Account account = new Account();
    account.setName("A Name");

    assertNotEquals(null, account.getJSON());
  }

  @Test
  public void query_success() {
    String responseJSON = "{\"totalSize\":13,\"done\":true,"
                          + "\"records\":[{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000008DqqoQAC\"},\"Id\":\"5003V000008DqqoQAC\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V5w5QAC\"},\"Id\":\"5003V000009V5w5QAC\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6IOQA0\"},\"Id\":\"5003V000009V6IOQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6IPQA0\"},\"Id\":\"5003V000009V6IPQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V689QAC\"},\"Id\":\"5003V000009V689QAC\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V68AQAS\"},\"Id\":\"5003V000009V68AQAS\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6MQQA0\"},\"Id\":\"5003V000009V6MQQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6MRQA0\"},\"Id\":\"5003V000009V6MRQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6N9QAK\"},\"Id\":\"5003V000009V6N9QAK\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6NAQA0\"},\"Id\":\"5003V000009V6NAQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6NOQA0\"},\"Id\":\"5003V000009V6NOQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V6NPQA0\"},\"Id\":\"5003V000009V6NPQA0\"},{\"attributes\":{\"type\":\"Case\",\"url\":\"/services/data/v50.0/sobjects/Case/5003V000009V5w6QAC\"},\"Id\":\"5003V000009V5w6QAC\"}]}}]}\n";

  }

  @Test
  public void queryExperiment() throws IOException {
    String jsonString = "[{\"attributes\":{\"type\":\"Account\",\"url\":\"/services/data/v50"
                        + ".0/sobjects/Account/0013V0000082ALKQA2\"},\"Id\":\"0013V0000082ALKQA2\"},{\"attributes\":{\"type\":\"Account\",\"url\":\"/services/data/v50.0/sobjects/Account/0013V0000082ALLQA2\"},\"Id\":\"0013V0000082ALLQA2\"}]";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(jsonString);
    ObjectReader reader = mapper.readerFor(new TypeReference<List<Account>>() {});
    List<Account> returnList = reader.readValue(jsonNode);
    assertNotNull(jsonNode);
  }


}