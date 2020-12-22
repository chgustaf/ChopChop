package com.examples.caseupdater;

import com.examples.caseupdater.client.SalesforceCaseClient;
import com.examples.caseupdater.client.domain.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.ImprovedSalesforceClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class CaseUpdate {


  public static void main(String[] args) throws IOException, AuthenticationException {
    SalesforceCaseClient client = new SalesforceCaseClient();
    ImprovedSalesforceClient improvedSalesforceClient = new ImprovedSalesforceClient(client.client);

  /*  client.getAllAccounts();

    Attributes attributes = new Attributes();
    attributes.setType("Account");
    attributes.setReferenceId("ref1");

    Account account = new Account();
    account.setName("Test Account "+System.nanoTime());
    account.setAttributes(attributes);

    Attributes attributes2 = new Attributes();
    attributes2.setType("Account");
    attributes2.setReferenceId("ref2");
    Account account2 = new Account();
    account2.setName("Test Account 2 "+System.nanoTime());
    account2.setAttributes(attributes2);

    List<Account> accounts = new ArrayList<>();
    accounts.add(account);
    accounts.add(account2);

    List<Account> response = client.createAccounts(accounts);


    List<String> ids = new ArrayList<>();
    for (Account account1 : response) {
      ids.add(account1.getId());
    }

    List<String> fields = new ArrayList<>();
    fields.add("id");*/
    /*
    Account account1 = new Account();
    account1.setName("Shit " + System.nanoTime());
    CallResult callResult = client.createAccount(account1);
    System.out.println("callResult.successes "+callResult.successes);*/

    // Create 3 cases
    JSONObject attribute = new JSONObject();
    attribute.put("type", "Account");
    JSONObject object = new JSONObject();
    object.put("Name", "AccountName");
    object.put("attributes", attribute);

    // Upsert 3 cases ( 1 is a new case)
    ObjectMapper mapper = new ObjectMapper();
    Account account1 = new Account();
    Account account2 = new Account();
    Account account3 = new Account();
    List<Account> accounts = new ArrayList<>();
    accounts.add(account1);
    accounts.add(account2);
    accounts.add(account3);
    List<String> accountStrings = new ArrayList<>();
    accounts.forEach(account -> {
      try {
        accountStrings.add(mapper.writeValueAsString(account));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    });
    improvedSalesforceClient.create(accountStrings, false);


    // Read the two new created cases

    // Query all (5) cases

    // Delete all cases

    //System.out.println(improvedSalesforceClient.read(ids, fields, "account"));

  }
}
