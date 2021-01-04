package com.examples.caseupdater;

import com.examples.caseupdater.client.CompositeBatchTransaction;
import com.examples.caseupdater.client.domain.Account;
import com.examples.caseupdater.client.domain.Case;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.SalesforceCompositeBatchClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CaseUpdate {


  public static void main(String[] args) throws IOException, AuthenticationException {
    SalesforceCompositeBatchClient
        salesforceCompositeBatchClient = new SalesforceCompositeBatchClient();

    Account account1 = new Account();
    account1.setName("Test Account Ompa "+System.currentTimeMillis());
    Account account2 = new Account();
    account2.setName("2nd Account Lumpa "+System.currentTimeMillis());
    List<Account> accounts = new ArrayList<>();
    accounts.add(account1);
    accounts.add(account2);
    accounts = createAccounts(accounts, salesforceCompositeBatchClient);
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
    cases = createCases(cases, salesforceCompositeBatchClient);

    account1 = get(account1, salesforceCompositeBatchClient);
    System.out.println("Account retrieved " + account1);

    account1.setName("New Test Account " + System.currentTimeMillis());
    account1 = update(account1, salesforceCompositeBatchClient);
    System.out.println("Account updated " + account1);

    //account1 = delete(account1, salesforceCompositeBatchClient);
    //System.out.println("This account1 was deleted " + account1);

  }

  private static Account create(Account account, SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
      transaction.create(account);
      if (!transaction.execute()) {
        System.out.println("Unable to create Account");
        return null;
      }

    return transaction.getRecord(account.getReferenceId(), account.getClass());
  }

  private static List<Account> createAccounts(List<Account> accounts,
                                SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    for (Account account : accounts) {
      transaction.create(account);
    }
    if (!transaction.execute()) {
      System.out.println("Unable to create Account");
      return null;
    }

    List<Account> returnAccounts = new ArrayList<>();
    for (Account account : accounts) {
      returnAccounts.add(transaction.getRecord(account.getReferenceId(), account.getClass()));
    }
    return returnAccounts;
  }


  private static List<Case> createCases(List<Case> cases,
                                   SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {

    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    for (Case caze : cases) {
      transaction.create(caze);
    }
    if (!transaction.execute()) {
      System.out.println("Unable to create Account");
      return null;
    }

    List<Case> returnCases = new ArrayList<>();
    for (Case caze : cases) {
      returnCases.add(transaction.getRecord(caze.getReferenceId(), caze.getClass()));
    }
    return returnCases;
  }

  private static Account get(Account account, SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    transaction.get(account);
    if (!transaction.execute()) {
      System.out.println("Unable to get Account");
      return null;
    }

    return transaction.getRecord(account.getReferenceId(), account.getClass());
  }

  private static Account update(Account account, SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    transaction.update(account);
    if (!transaction.execute()) {
      System.out.println("Unable to update Account");
      return null;
    }

    return transaction.getRecord(account.getReferenceId(), account.getClass());
  }

  private static Account delete(Account account, SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    transaction.delete(account);
    if (!transaction.execute()) {
      System.out.println("Unable to delete Account");
      return null;
    }

    return transaction.getRecord(account.getReferenceId(), account.getClass());
  }

}
