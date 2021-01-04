package com.examples.caseupdater;

import com.examples.caseupdater.client.CompositeBatchTransaction;
import com.examples.caseupdater.client.SalesforceCaseClient;
import com.examples.caseupdater.client.domain.Account;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.ImprovedSalesforceClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CaseUpdate {


  public static void main(String[] args) throws IOException, AuthenticationException {
    SalesforceCaseClient client = new SalesforceCaseClient();
    ImprovedSalesforceClient improvedSalesforceClient = new ImprovedSalesforceClient(client.client);
    // TODO Merge the Improved Salesforce client with the SalesforceClient

    Account account = new Account();
    account.setName("Test Account "+System.currentTimeMillis());
    Account account2 = new Account();
    account2.setName("2nd Account "+System.currentTimeMillis());
    List<Account> accounts = new ArrayList<>();
    accounts.add(account);
    accounts.add(account2);
    accounts = create(accounts, improvedSalesforceClient);
    // TODO Find a way so there is no need to pass around the client
    account = accounts.get(0);
    account2 = accounts.get(1);
    System.out.println("Two account created " + account + " and " + account2);

    account = get(account, improvedSalesforceClient);
    System.out.println("Account retrieved " + account);

    account.setName("New Test Account " + System.currentTimeMillis());
    account = update(account, improvedSalesforceClient);
    System.out.println("Account updated " + account);

    account = delete(account, improvedSalesforceClient);
    System.out.println("This account was deleted " + account);

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

  private static List<Account> create(List<Account> accounts,
                                ImprovedSalesforceClient improvedSalesforceClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(improvedSalesforceClient);
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
