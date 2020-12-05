package com.examples.caseupdater;

import static com.salesforce.rest.SalesforceClient.AuthenticationFlow.USER_PASSWORD;

import com.examples.caseupdater.domain.Account;
import com.examples.caseupdater.domain.Case;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.SalesforceClient;
import java.io.IOException;

public class SalesforceCaseClient {

  SalesforceClient client;

  public SalesforceCaseClient() {
    try {
      client = new SalesforceClient(USER_PASSWORD);
    } catch (AuthenticationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String createAccount(Account account) throws IOException, AuthenticationException {
    String accountJson = new ObjectMapper().writeValueAsString(account);
    String response = client.post("account", accountJson);
    return response;
  }

  public void createCase(Case aCase) throws IOException, AuthenticationException {
    String caseJson = new ObjectMapper().writeValueAsString(aCase);
    client.post("case", caseJson);
  }

  public void getCasesOfAccount(String accountId) throws IOException, AuthenticationException {
    client.query("SELECT Id FROM Account WHERE Id = \'"+accountId+"\'");
  }

  public void updateCase() {
    try {
      Case aCase = new ObjectMapper().readValue(client.query("SELECT Id FROM Case"), Case.class);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (AuthenticationException e) {
      e.printStackTrace();
    }
  }
}
