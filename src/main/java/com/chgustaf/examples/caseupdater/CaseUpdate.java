package com.chgustaf.examples.caseupdater;

import static com.chgustaf.salesforce.client.composite.batch.Operations.delete;
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
        new Query<>("SELECT id FROM Primary_Test_Object__c WHERE Test_Percentage__c != null LIMIT"
                    + " 1",
            Primary_Test_Object__c.class);
    List<Primary_Test_Object__c> queriedTestObjects = query(query1, salesforceCompositeBatchClient);
    Primary_Test_Object__c primary_test_object__c = queriedTestObjects.get(0);
    primary_test_object__c.setId("1234");
    primary_test_object__c = delete(primary_test_object__c, salesforceCompositeBatchClient);
  }
}
