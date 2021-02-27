package com.chgustaf.examples.caseupdater;

import static com.chgustaf.salesforce.client.composite.batch.Operations.get;
import static com.chgustaf.salesforce.client.composite.batch.Operations.query;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Primary_Test_Object__c;
import com.chgustaf.salesforce.client.composite.domain.Query;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CaseUpdate {


  public static void main(String[] args)
      throws IOException, AuthenticationException, TransactionException {
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = new SalesforceCompositeBatchClient();

    Primary_Test_Object__c primaryTestObject = new Primary_Test_Object__c();
    primaryTestObject.setTestEmail("contact@innovationmadness.com");
    primaryTestObject.setTestCheckbox(true);
    primaryTestObject.setTestCurrency(123.456f);
    primaryTestObject.setTestDate(Date.from(Instant.now()));
    primaryTestObject.setTestDateTime(Instant.now().atZone(ZoneId.of("UTC")));
    primaryTestObject.setTestGeolocationLatitude(59.334591d);
    primaryTestObject.setTestGeolocationLatitude(18.063242d);
    primaryTestObject.setTestMultiSelectPicklist("Test Value One; Test Value Two");
    primaryTestObject.setTestNumber(1234);
    primaryTestObject.setTestPercentage(98.89f);
    primaryTestObject.setTestPhone("+46727313212");
    primaryTestObject.setTestPicklist("Value One");
    primaryTestObject.setTestText("Text");
    primaryTestObject.setTestTextArea("Text Area");
    primaryTestObject.setTestTextAreaLong("Text Area Long");
    primaryTestObject.setTestTextAreaRich("<html><h1>Text Area Rich</h1></html>");
    primaryTestObject.setTestTextEncrypted("Test encrypted");
    primaryTestObject.setTestTime("11:00:01");
    primaryTestObject.setTestUrl("https://google.com");

    Primary_Test_Object__c secondaryTestObject = new Primary_Test_Object__c();
    secondaryTestObject.setTestEmail("contact1@innovationmadness.com");
    secondaryTestObject.setTestCheckbox(true);
    secondaryTestObject.setTestCurrency(123.456f);
    secondaryTestObject.setTestDate(Date.from(Instant.now()));
    secondaryTestObject.setTestDateTime(Instant.now().atZone(ZoneId.of("UTC")));
    secondaryTestObject.setTestGeolocationLatitude(59.334591d);
    secondaryTestObject.setTestGeolocationLatitude(18.063242d);
    secondaryTestObject.setTestMultiSelectPicklist("Test Value One; Test Value Two");
    secondaryTestObject.setTestNumber(1234);
    secondaryTestObject.setTestPercentage(98.89f);
    secondaryTestObject.setTestPhone("+46727313212");
    secondaryTestObject.setTestPicklist("Value Two");
    secondaryTestObject.setTestText("Text");
    secondaryTestObject.setTestTextArea("Text Area");
    secondaryTestObject.setTestTextAreaLong("Text Area Long");
    secondaryTestObject.setTestTextAreaRich("<html><h1>Text Area Rich</h1></html>");
    secondaryTestObject.setTestTextEncrypted("Test encrypted");
    secondaryTestObject.setTestTime("11:00:01");
    secondaryTestObject.setTestUrl("https://google.com");
    List<Primary_Test_Object__c> testObjects = new ArrayList<>();
    testObjects.add(primaryTestObject);
    testObjects.add(secondaryTestObject);

    Query<Primary_Test_Object__c> query1 =
        new Query<>("SELECT id FROM Primary_Test_Object__c WHERE Test_Percentage__c != null LIMIT 2",
            Primary_Test_Object__c.class);
    List<Primary_Test_Object__c> queriedTestObjects = query(query1, salesforceCompositeBatchClient);

    System.out.println(primaryTestObject.getAllFields());
    List<Primary_Test_Object__c> getList = get(queriedTestObjects,
        salesforceCompositeBatchClient);

    for (Primary_Test_Object__c obj : getList) {
      System.out.println(obj.toString());
    }


    /*try {
      //primaryTestObject = create(primaryTestObject, salesforceCompositeBatchClient);
    } catch (TransactionException e) {
      e.printStackTrace();
    }*/


/*
    String queryString = "SELECT Test_Checkbox__c, Test_Currency__c, Test_Date__c, "
                        + "Test_Datetime__c, "
                   + "Test_Email__c, Test_Formula_Field__c, Test_Geolocation__latitude__s, "
                   + "Test_Geolocation__longitude__s, Test_Multiselect_Picklist__c, Name, "
                   + "Test_Number__c, Test_Percentage__c, Test_Phone__c, Test_Picklist__c, "
                   + "Test_Text__c, Test_Text_Area__c, Test_Text_Area_Long__c, "
                   + "Test_Text_Area_Rich__c, Test_Text_Encrypted__c, Test_Time__c, Test_URL__c "
                   + "FROM Primary_Test_Object__c";
    Query<Primary_Test_Object__c> query = new Query<>(queryString, Primary_Test_Object__c.class);
    List<Primary_Test_Object__c> queryResultList = null;
    try {
      queryResultList = query(query, salesforceCompositeBatchClient);
    } catch (TransactionException e) {
      e.printStackTrace();
    }
    queryResultList.stream().forEach(pto -> System.out.println("Here is one record of the Primary "
                           /*                                + "Test Object " + pto));*//*
    String queryStringCases = "SELECT id, subject, accountId, origin FROM Case LIMIT 25";
    List<Case> caseList = null;
    try {
      caseList = query(
          new Query<>(queryStringCases, Case.class),
          salesforceCompositeBatchClient);
    } catch (TransactionException e) {
      e.printStackTrace();
    }
    System.out.println("Here are the cases " + caseList.size());

    caseList.stream().forEach(cas -> cas.setSubject(cas.getSubject()+"0"));
    caseList.stream().forEach(cas -> System.out.println(cas.subject));

    List<Case> cases = updateRecords(caseList, salesforceCompositeBatchClient);

    Primary_Test_Object__c testObject2 = new Primary_Test_Object__c();
    testObject2.setTestNumber(2);
    Primary_Test_Object__c testObject3 = new Primary_Test_Object__c();
    testObject3.setTestNumber(3);

    ArrayList<Primary_Test_Object__c> testObjects = new ArrayList<>();
    testObjects.add(testObject2);
    testObjects.add(testObject3);
    List<Primary_Test_Object__c> returnList = createRecords(testObjects,
        salesforceCompositeBatchClient);

    returnList.forEach(obj -> obj.setTestText("test"));
    updateRecords(returnList, salesforceCompositeBatchClient);
*/
/*
    String queryString = "SELECT id, subject, accountId FROM Case LIMIT 100";
    Query<Case> caseQuery = new Query<>(queryString, Case.class);
    List<Case> casesAgain = null;
    try {
      casesAgain = query(caseQuery, salesforceCompositeBatchClient);
      casesAgain.forEach(obj -> obj.setSubject("Tester"));
      updateRecords(casesAgain, salesforceCompositeBatchClient);
    } catch (TransactionException e) {
      System.out.println("This is an exception catch");
      e.printStackTrace();
    }
*/

/*
    String queryStringTest = "SELECT id, Test_Formula_Field__c FROM Primary_Test_Object__c LIMIT "
                             + "10";
    List<Primary_Test_Object__c> testList = null;
    try {
      testList = query(
          new Query<>(queryStringTest, Primary_Test_Object__c.class),
          salesforceCompositeBatchClient);
    } catch (TransactionException e) {
      e.printStackTrace();
    }
    System.out.println("Here are the cases " + testList.size());

    // TODO: Write tests for queries
/*
    Query query = new Query<>(Account.class);
    query.setQuery(URLEncoder.encode("SELECT id, name FROM Account",
        StandardCharsets.UTF_8));
    AsynchronousOperations.queryAsync(query, salesforceCompositeBatchClient);
/*
    Query query2 = new Query<Account>();
    query2.setQuery(URLEncoder.encode("SELECT id, description FROM Account",
        StandardCharsets.UTF_8));
    AsynchronousOperations.queryAsync(query2, salesforceCompositeBatchClient);
*/ /*
    Query<Account> query3 = new Query<>(Account.class);
    query3.setQuery(URLEncoder.encode("SELECT id, description, name FROM Account",
        StandardCharsets.UTF_8));
    List<Account> accounts1 = query(query3, salesforceCompositeBatchClient);
    System.out.println("Account list size " + accounts1.size());
    System.out.println("The first acounts name " + accounts1.get(0).name);
*/
   /* Query<Case> caseQuery = new Query<>(Case.class);
    caseQuery.setQuery(URLEncoder.encode("SELECT id FROM Case", StandardCharsets.UTF_8));
    List<Case> caseList = query(caseQuery, salesforceCompositeBatchClient);
    System.out.println("CaseList " + caseList);
            /*AsynchronousOperations.queryAsync(caseQuery, salesforceCompositeBatchClient)
                .thenApply(x -> {System.out.println("NUMBER OF CASE RECORDS "+x); return x;});
    System.out.println("Case list size " + caseList.size());
    System.out.println("The first case subject " + caseList.get(0).subject);



    Account account1 = new Account();
    account1.setName("Test Account Ompa "+System.currentTimeMillis());
    Operations.create(account1, salesforceCompositeBatchClient);
    Account account2 = new Account();
    account2.setName("2nd Account Lumpa "+System.currentTimeMillis());
    List<Account> accounts = new ArrayList<>();
    accounts.add(account1);
    accounts.add(account2);
    accounts = Operations.createRecords(accounts, salesforceCompositeBatchClient);
    // TODO Find a way so there is no need to pass around the exampleclient
    account1 = accounts.get(0);
    account2 = accounts.get(1);
    System.out.println("Good gracious account1 created " + account1 + " and " + account2);

    Case case1 = new Case();
    Case case2 = new Case();
    case1.setSubject("Humpa 1");
    case2.setSubject("Lumpa 2");
    case1.setAccountId(account1.getId());
    case2.setAccountId(account2.getId());
    List<Case> cases = new ArrayList<>();
    cases.add(case1);
    cases.add(case2);
    cases = Operations.createRecords(cases, salesforceCompositeBatchClient);
    System.out.println("Created Cases " + cases);

    account1 = Operations.get(account1, salesforceCompositeBatchClient);
    System.out.println("Account retrieved " + account1);

    account1.setName("New Test Account " + System.currentTimeMillis());
    account1 = Operations.update(account1, salesforceCompositeBatchClient);
    System.out.println("Account updated " + account1);

    // TODO: Add support for calling custom web services

    //Account account = delete(accountList.get(0), salesforceCompositeBatchClient);
    //System.out.println("Account has been deleted " + account.getStatusCode());

    //account1 = delete(account1, salesforceCompositeBatchClient);
    //System.out.println("This account1 was deleted " + account1);
*/
    /*Account account = new Account();
    account.setName("Test Account 1");
    account.setDescription("This account will own a shit tonne of cases");
    account = create(account, salesforceCompositeBatchClient);
    if (account.getId() != null) {
      for(int i=0; i< 1000; i++) {
        Case c = new Case();
        c.setSubject("Test case " + i);
        c.setAccountId(account.getId());
        c = create(c, salesforceCompositeBatchClient);
        if (!c.getSuccess()) {
          c.getErrors().stream().forEach(err -> System.out.println(err.toString()));
          System.out.println("Didn't work. Exiting ");
          break;
        }
      }
    }*/
  }


}
