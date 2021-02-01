package com.chgustaf.examples.caseupdater;

import static com.chgustaf.salesforce.client.composite.batch.Operations.create;

import com.chgustaf.examples.caseupdater.exampleclient.domain.Primary_Test_Object__c;
import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CaseUpdate {


  public static void main(String[] args)
      throws IOException, AuthenticationException, ExecutionException, InterruptedException {
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = new SalesforceCompositeBatchClient();

    Primary_Test_Object__c primaryTestObjectC = new Primary_Test_Object__c();
    primaryTestObjectC.setTestEmail("contact@innovationmadness.com");
    primaryTestObjectC.setTestCheckbox(true);

    try {
      Primary_Test_Object__c primaryTestObjectC1 = create(primaryTestObjectC,
          salesforceCompositeBatchClient);
    } catch (TransactionException e) {
      e.printStackTrace();
    }

    System.out.println("Primary test Object " + primaryTestObjectC);
    /*
    Query query = new Query<Account>();
    query.setQuery(URLEncoder.encode("SELECT id, name FROM Account",
        StandardCharsets.UTF_8));
    AsynchronousOperations.queryAsync(query, salesforceCompositeBatchClient);

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

    Query<Case> caseQuery = new Query<>(Case.class);
    caseQuery.setQuery(URLEncoder.encode("SELECT id FROM Case", StandardCharsets.UTF_8));
    List<Case> caseList = query(caseQuery, salesforceCompositeBatchClient);
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
  }

}
