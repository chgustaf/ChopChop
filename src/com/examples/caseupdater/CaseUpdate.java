package com.examples.caseupdater;

import com.examples.caseupdater.client.SalesforceCaseClient;
import com.examples.caseupdater.client.domain.Account;
import com.examples.caseupdater.client.domain.CallResult;
import com.examples.caseupdater.client.dto.Attributes;
import com.salesforce.exceptions.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CaseUpdate {


  public static void main(String[] args) throws IOException, AuthenticationException {
    SalesforceCaseClient client = new SalesforceCaseClient();

    client.getAllAccounts();

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

    for (Account account1 : response) {
      System.out.println(account1);
      client.deleteAccount(account1);
    }

    Account account1 = new Account();
    account1.setName("Shit " + System.nanoTime());
    CallResult callResult = client.createAccount(account1);
    System.out.println("callResult.successes "+callResult.successes);
  }
}
