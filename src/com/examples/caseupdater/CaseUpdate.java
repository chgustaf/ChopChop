package com.examples.caseupdater;

import com.examples.caseupdater.client.CompositeBatchTransaction;
import com.examples.caseupdater.client.SalesforceCaseClient;
import com.examples.caseupdater.client.domain.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.ImprovedSalesforceClient;
import java.io.IOException;

public class CaseUpdate {


  public static void main(String[] args) throws IOException, AuthenticationException {
    SalesforceCaseClient client = new SalesforceCaseClient();
    ImprovedSalesforceClient improvedSalesforceClient = new ImprovedSalesforceClient(client.client);
    ObjectMapper mapper = new ObjectMapper();
    // Create 3 cases
   /* JSONObject attribute = new JSONObject();
    attribute.put("type", "Account");
    JSONObject object = new JSONObject();
    object.put("Name", "AccountName");
    object.put("attributes", attribute);

    // Upsert 3 cases ( 1 is a new case)
    ObjectMapper mapper = new ObjectMapper();
    Account account1 = new Account("First Account " + System.nanoTime());
    Account account2 = new Account("Second Account " + System.nanoTime());
    Account account3 = new Account("Third Account " + System.nanoTime());
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
    improvedSalesforceClient.create(accountStrings, true);

    // Upsert 2 cases (1 create, 1 update)
    List<Account> accountList = new ArrayList<>();
    account1.setName("Premier Account");
    accountList.add(account1);
    accountList.add(new Account("Fourth Account"));

    List<String> accountStrings1 = new ArrayList<>();
    accountList.forEach(account -> {
      try {
        accountStrings1.add(mapper.writeValueAsString(account));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    });*/

    // Create 3 cases

    // Upsert 3 cases ( 1 is a new case)

    // Update 2 cases

    // Query all cases

    // Delete all cases

/*
    Account account1 = new Account();
    account1.setName("Composite Account " + System.nanoTime());
    CompositeRequest compositeRequest =
        new CompositeRequestBuilder().setMethod("POST").setBody(mapper.writeValueAsString(account1)).setUrl("/services/data/v50.0/sobjects/Account").createCompositeRequest();
    CompositeRequest[] compositeRequests = new CompositeRequest[1];
    compositeRequests[0] = compositeRequest;
    Request request =
        new RequestBuilder().setCompositeRequest(compositeRequests).setAllOrNone(true).createRequest();
    System.out.println(improvedSalesforceClient.compositeCall(request.toString()));
*/
   /* Account account = new Account();
    account.setName("eajYGvBJUFwSVmIGHtEznoT4iZHxXEzBzvNKKkNPDvSfJPuo1rB32Dh6oeaJhDSSSlxZgXGdwft6dvCkvzB25VUxU7DsgwfAA9rkF2fJUx944hjmHyKTVNxeSFSIptvDUdf2R4dkyF855wa8FVa9mX7sMxAd1VrxMKQLSELGv47MfG");

    CompositeTransaction compositeTransaction = new CompositeTransaction(improvedSalesforceClient);
    compositeTransaction.create(account);
    Response response = compositeTransaction.execute();
*/

    Account account = new Account();
    account.setName("Test Account "+System.currentTimeMillis());
    account = create(account, improvedSalesforceClient);
    System.out.println("Account created " + account);

    account = get(account, improvedSalesforceClient);
    System.out.println("Account retrieved " + account);

    account.setName("New Test Account " + System.currentTimeMillis());
    account = update(account, improvedSalesforceClient);
    System.out.println("Account updated " + account);

    account = delete(account, improvedSalesforceClient);
    System.out.println("This account was deleted " + account);


/*
    Account account = new Account();
    account.setId();*/

    // Read the two new created cases


    // Query all (5) cases

    // Delete all cases

    //System.out.println(improvedSalesforceClient.read(ids, fields, "account"));

  }

  private static Account create(Account account, ImprovedSalesforceClient improvedSalesforceClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(improvedSalesforceClient);
      transaction.create(account);
      if (!transaction.execute()) {
        System.out.println("Unable to create Account");
        return null;
      }

    return transaction.getRecord(account.getReferenceId(), account.getClass());
  }

  private static Account get(Account account, ImprovedSalesforceClient improvedSalesforceClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(improvedSalesforceClient);
    transaction.get(account);
    if (!transaction.execute()) {
      System.out.println("Unable to get Account");
      return null;
    }

    return transaction.getRecord(account.getReferenceId(), account.getClass());
  }

  private static Account update(Account account, ImprovedSalesforceClient improvedSalesforceClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(improvedSalesforceClient);
    transaction.update(account);
    if (!transaction.execute()) {
      System.out.println("Unable to update Account");
      return null;
    }

    return transaction.getRecord(account.getReferenceId(), account.getClass());
  }

  private static Account delete(Account account, ImprovedSalesforceClient improvedSalesforceClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(improvedSalesforceClient);
    transaction.delete(account);
    if (!transaction.execute()) {
      System.out.println("Unable to delete Account");
      return null;
    }

    return transaction.getRecord(account.getReferenceId(), account.getClass());
  }
}
