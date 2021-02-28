package com.chgustaf.salesforce.client.composite.batch;

import static com.chgustaf.salesforce.client.TestUtils.getCompositeBatchTransaction;
import static com.chgustaf.salesforce.client.composite.batch.Operations.create;
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
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OperationsTest {

  @Test
  void getRecord_success() throws TransactionException, IOException, AuthenticationException {
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
  void getRecord_fail() throws TransactionException, IOException, AuthenticationException {
    String id = "0013V000009ikVtQAI";
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"UNKNOWN_EXCEPTION\",\"message\":\"An unexpected error occurred. "
                          + "Please include this ErrorId if you contact support: 8425053-14906 "
                          + "(608530858)\"}],\"statusCode\":500}]}";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(responseJson);
    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setId(id);
    TransactionException exception = assertThrows(TransactionException.class,
        () -> {get(testObject, salesforceCompositeBatchClient);});
    System.out.println(exception.getMessage());
    assertEquals("UNKNOWN_EXCEPTION", exception.getCode());
  }

  @Test
  void getRecords_success() throws TransactionException, IOException, AuthenticationException {
    String id1 = "a0009000003yniUAAQ";
    String id2 = "a0009000003yniZAAQ";
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":200,\"result\":\n"
                          + "{\"attributes\":{\"type\":\"Primary_Test_Object__c\",\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/a0009000003yniUAAQ\"},\"Test_Checkbox__c\":true,\"Test_Currency__c\":123.456,\"Test_Date__c\":\"2021-02-17\",\"Test_Datetime__c\":\"2021-02-17T23:04:12.000+0000\",\"Test_Email__c\":\"contact@innovationmadness.com\",\"Test_Formula_Field__c\":\"A-00069\",\"Test_Geolocation__Latitude__s\":18.063242,\"Test_Geolocation__Longitude__s\":0.0,\"Test_Multiselect_Picklist__c\":\"Test Value One;Test Value Two\",\"Name\":\"A-00069\",\"Test_Number__c\":1234.0,\"Test_Percentage__c\":98.89,\"Test_Phone__c\":\"+46727313212\",\"Test_Picklist__c\":\"Value One\",\"Test_Text__c\":\"Text\",\"Test_Text_Area__c\":\"Text Area Long\",\"Test_Text_Area_Long__c\":\"Text Area Long\",\"Test_Text_Area_Rich__c\":\"<h1>Text Area Rich</h1>\",\"Test_Text_Encrypted__c\":\"XXXXXXXXXXXXXX\",\"Test_Time__c\":\"10:00:01.000Z\",\"Test_URL__c\":\"https://google.com\",\"Id\":\"a0009000003yniUAAQ\"}},{\"statusCode\":200,\"result\":\n"
                          + "{\"attributes\":{\"type\":\"Primary_Test_Object__c\","
                          + "\"url\":\"/services/data/v50.0/sobjects/Primary_Test_Object__c/a0009000003yniZAAQ\"},\"Test_Checkbox__c\":true,\"Test_Currency__c\":123.456,\"Test_Date__c\":\"2021-02-17\",\"Test_Datetime__c\":\"2021-02-17T23:04:25.000+0000\",\"Test_Email__c\":\"contact@innovationmadness.com\",\"Test_Formula_Field__c\":\"A-00070\",\"Test_Geolocation__Latitude__s\":18.063242,\"Test_Geolocation__Longitude__s\":0.0,\"Test_Multiselect_Picklist__c\":\"Test Value One;Test Value Two\",\"Name\":\"A-00070\",\"Test_Number__c\":1234.0,\"Test_Percentage__c\":98.89,\"Test_Phone__c\":\"+46727313212\",\"Test_Picklist__c\":\"Value One\",\"Test_Text__c\":\"Text\",\"Test_Text_Area__c\":\"Text Area Long\",\"Test_Text_Area_Long__c\":\"Text Area Long\",\"Test_Text_Area_Rich__c\":\"<h1>Text Area Rich</h1>\",\"Test_Text_Encrypted__c\":\"XXXXXXXXXXXXXX\",\"Test_Time__c\":\"10:00:01.000Z\",\"Test_URL__c\":\"https://google.com\",\"Id\":\"a0009000003yniZAAQ\"}}]}\n";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(responseJson);

    Primary_Test_Object__c testObject1 = new Primary_Test_Object__c();
    testObject1.setId(id1);
    Primary_Test_Object__c testObject2 = new Primary_Test_Object__c();
    testObject2.setId(id2);
    List<Primary_Test_Object__c> testObjects = new ArrayList<>();
    testObjects.add(testObject1);
    testObjects.add(testObject2);
    testObjects = get(testObjects, salesforceCompositeBatchClient);
    assertEquals(2, testObjects.size());
    for (Primary_Test_Object__c testObject : testObjects) {
      assertTrue(testObject.getTestCheckbox());
    }
  }

  // TODO: Continue write tests for the Operations class. Update, create and delete left.

  @Test
  void getRecords_fail() throws TransactionException, IOException, AuthenticationException {
    String id1 = "a0009000003yniUAAQ";
    String id2 = "a0009000003yniZAAQ";
    String responseJson = "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode"
                          + "\":\"INVALID_FIELD\",\"message\":\"\\nTest_Multiselect_Picklist__c,"
                          + "Name,Test_Number__c,Test_Percentage2__c,Test_Phone__c\\nERROR at Row:1:Column:218\\nNo such column &#39;Test_Percentage2__c&#39; on entity &#39;Primary_Test_Object__c&#39;. If you are attempting to use a custom field, be sure to append the &#39;__c&#39; after the custom field name. Please reference your WSDL or the describe call for the appropriate names.\"}],\"statusCode\":400},{\"result\":[{\"errorCode\":\"INVALID_FIELD\",\"message\":\"\\nTest_Multiselect_Picklist__c,Name,Test_Number__c,Test_Percentage2__c,Test_Phone__c\\nERROR at Row:1:Column:218\\nNo such column &#39;Test_Percentage2__c&#39; on entity &#39;Primary_Test_Object__c&#39;. If you are attempting to use a custom field, be sure to append the &#39;__c&#39; after the custom field name. Please reference your WSDL or the describe call for the appropriate names.\"}],\"statusCode\":400}]}\n";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(responseJson);

    Primary_Test_Object__c testObject1 = new Primary_Test_Object__c();
    testObject1.setId(id1);
    Primary_Test_Object__c testObject2 = new Primary_Test_Object__c();
    testObject2.setId(id2);
    List<Primary_Test_Object__c> testObjects = new ArrayList<>();
    testObjects.add(testObject1);
    testObjects.add(testObject2);
    TransactionException exception = assertThrows(TransactionException.class,
        () -> {get(testObjects, salesforceCompositeBatchClient);});
    assertTrue(exception.getMessage().contains("No such column"));
    assertEquals("MULTIPLE_ERRORS", exception.getCode());
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
    //TODO: Fix this multi part test method
  }

  @Test
  void createRecord_success() throws TransactionException, IOException, AuthenticationException {
    String id = "0013V000009id1YQAQ";
    String responseJson = "{\"hasErrors\":false,\"results\":[{\"statusCode\":201,\"result\":\n"
                          + "{\"id\":\""+id+"\",\"success\":true,\"errors\":[]}}]}";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(responseJson);

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setTestNumber(123);
    testObject = create(testObject, salesforceCompositeBatchClient);
    assertEquals(id, testObject.getId());
  }

  @Test
  void createRecord_fail() throws TransactionException, IOException, AuthenticationException {
    String id = "0013V000009id1YQAQ";
    String responseJson =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"INVALID_FIELD\",\"message\":\"No such column &#39;Test_Text1__c&#39; on sobject of type Primary_Test_Object__c\"}],\"statusCode\":400}]}\n";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(responseJson);

    Primary_Test_Object__c testObject = new Primary_Test_Object__c();
    testObject.setTestNumber(123);

    TransactionException exception = assertThrows(TransactionException.class, () -> create(testObject,
        salesforceCompositeBatchClient));
    assertTrue(exception.getMessage().contains("INVALID_FIELD"));
  }

  @Test
  void createRecords_success() throws TransactionException, IOException, AuthenticationException {
    String id1 = "a0009000005IjFRAA0";
    String id2 = "a0009000005IjFSAA0";
    String responseJson =
        "{\"hasErrors\":false,\"results\":[{\"statusCode\":201,\"result\":\n"
            + "{\"id\":\"a0009000005IjFRAA0\",\"success\":true,\"errors\":[]}},{\"statusCode\":201,\"result\":\n"
            + "{\"id\":\"a0009000005IjFSAA0\",\"success\":true,\"errors\":[]}}]}\n";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(responseJson);

    Primary_Test_Object__c testObject1 = new Primary_Test_Object__c();
    testObject1.setTestNumber(123);
    Primary_Test_Object__c testObject2 = new Primary_Test_Object__c();
    testObject2.setTestNumber(456);
    List<Primary_Test_Object__c> testObjects = new ArrayList<>();
    testObjects.add(testObject1);
    testObjects.add(testObject2);

    testObjects = create(testObjects, salesforceCompositeBatchClient);
    assertEquals(id1, testObjects.get(0).getId());
    assertEquals(id2, testObjects.get(1).getId());
  }

  @Test
  void createRecords_fail() throws TransactionException, IOException, AuthenticationException {
    String responseJson =
        "{\"hasErrors\":true,\"results\":[{\"result\":[{\"errorCode\":\"INVALID_FIELD\",\"message\":\"\\nTest_Phone__c,Test_Picklist__c,Test_Text1__c,Test_Text_Area__c\\n                               ^\\nERROR at Row:1:Column:268\\nNo such column &#39;Test_Text1__c&#39; on entity &#39;Primary_Test_Object__c&#39;. If you are attempting to use a custom field, be sure to append the &#39;__c&#39; after the custom field name. Please reference your WSDL or the describe call for the appropriate names.\"}],\"statusCode\":400},{\"result\":[{\"errorCode\":\"INVALID_FIELD\",\"message\":\"\\nTest_Phone__c,Test_Picklist__c,Test_Text1__c,Test_Text_Area__c\\n                               ^\\nERROR at Row:1:Column:268\\nNo such column &#39;Test_Text1__c&#39; on entity &#39;Primary_Test_Object__c&#39;. If you are attempting to use a custom field, be sure to append the &#39;__c&#39; after the custom field name. Please reference your WSDL or the describe call for the appropriate names.\"}],\"statusCode\":400}]}\n";
    SalesforceCompositeBatchClient salesforceCompositeBatchClient =
        mock(SalesforceCompositeBatchClient.class);
    when(salesforceCompositeBatchClient.compositeBatchCall(anyString())).thenReturn(responseJson);

    Primary_Test_Object__c testObject1 = new Primary_Test_Object__c();
    testObject1.setTestNumber(123);
    Primary_Test_Object__c testObject2 = new Primary_Test_Object__c();
    testObject2.setTestNumber(456);
    List<Primary_Test_Object__c> testObjects = new ArrayList<>();
    testObjects.add(testObject1);
    testObjects.add(testObject2);

    TransactionException exception = assertThrows(TransactionException.class, () -> create(testObjects,
        salesforceCompositeBatchClient));
    assertTrue(exception.getMessage().contains("INVALID_FIELD"));
  }
}
