package com.examples.caseupdater;

import com.examples.caseupdater.domain.Account;
import com.salesforce.exceptions.AuthenticationException;
import java.io.IOException;

public class CaseUpdate {


  public static void main(String[] args) throws IOException, AuthenticationException {
    SalesforceCaseClient client = new SalesforceCaseClient();
    Account account = new Account();
    account.setName("Test Account 1");

    String response = client.createAccount(account);
    System.out.println(response);

    // TODO: Finish this example client
  }






}
