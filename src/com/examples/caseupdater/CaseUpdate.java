package com.examples.caseupdater;

import static com.examples.caseupdater.client.Operations.create;
import static com.examples.caseupdater.client.Operations.createRecords;
import static com.examples.caseupdater.client.Operations.get;
import static com.examples.caseupdater.client.Operations.query;
import static com.examples.caseupdater.client.Operations.update;

import com.examples.caseupdater.client.domain.Account;
import com.examples.caseupdater.client.domain.Case;
import com.examples.caseupdater.client.domain.Query;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.SalesforceCompositeBatchClient;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CaseUpdate {


  public static void main(String[] args) throws IOException, AuthenticationException {
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = new SalesforceCompositeBatchClient();

    Account account1 = new Account();
    account1.setName("Test Account Ompa "+System.currentTimeMillis());
    create(account1, salesforceCompositeBatchClient);
    Account account2 = new Account();
    account2.setName("2nd Account Lumpa "+System.currentTimeMillis());
    List<Account> accounts = new ArrayList<>();
    accounts.add(account1);
    accounts.add(account2);
    accounts = createRecords(accounts, salesforceCompositeBatchClient);
    // TODO Find a way so there is no need to pass around the client
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
    cases = createRecords(cases, salesforceCompositeBatchClient);
    System.out.println("Created Cases " + cases);

    account1 = get(account1, salesforceCompositeBatchClient);
    System.out.println("Account retrieved " + account1);

    account1.setName("New Test Account " + System.currentTimeMillis());
    account1 = update(account1, salesforceCompositeBatchClient);
    System.out.println("Account updated " + account1);
    Query query = new Query();
    query.setQuery(URLEncoder.encode("SELECT id, name FROM Account",
        StandardCharsets.UTF_8));

    List<Account> accountList = query(query, salesforceCompositeBatchClient);
    System.out.println("Account List " + accountList);
    System.out.println(accountList.get(0));

    // TODO: Add support for custom rest web services

    //Account account = delete(accountList.get(0), salesforceCompositeBatchClient);
    //System.out.println("Account has been deleted " + account.getStatusCode());

    //account1 = delete(account1, salesforceCompositeBatchClient);
    //System.out.println("This account1 was deleted " + account1);

  }

}
