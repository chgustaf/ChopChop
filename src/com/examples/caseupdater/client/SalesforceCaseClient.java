package com.examples.caseupdater.client;

import static com.salesforce.rest.SalesforceClient.AuthenticationFlow.USER_PASSWORD;

import com.examples.caseupdater.client.domain.Account;
import com.examples.caseupdater.client.domain.CallResult;
import com.examples.caseupdater.client.domain.Case;
import com.examples.caseupdater.client.domain.Record;
import com.examples.caseupdater.client.domain.Records;
import com.examples.caseupdater.client.dto.RecordCreated;
import com.examples.caseupdater.client.dto.RecordsCreated;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.SalesforceClient;
import java.io.IOException;
import java.util.List;

public class SalesforceCaseClient {

  public SalesforceClient client;

  public SalesforceCaseClient() {
    try {
      client = new SalesforceClient(USER_PASSWORD);
    } catch (AuthenticationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private RecordsCreated createMultipleRecords(Records records, String objectName)
      throws IOException, AuthenticationException {

    String accountJson = new ObjectMapper().writeValueAsString(records);
    String response = client.postMultipleRecords(objectName, accountJson);
    RecordsCreated recordsCreated = new ObjectMapper().readValue(response, RecordsCreated.class);
    return recordsCreated;
  }

  private RecordCreated createRecord(String bodyJson, String objectName) throws IOException,
                                                               AuthenticationException {
    String response = client.postSingleRecord(objectName, bodyJson);
    RecordCreated recordCreated = new ObjectMapper().readValue(response, RecordCreated.class);

    return recordCreated;
  }


  public RecordsCreated createCases(Records<Case> cases)
      throws IOException, AuthenticationException {
    return createMultipleRecords(cases, "case");
  }

  public RecordCreated createCase(Case aCase) throws IOException, AuthenticationException {
    String caseJson = new ObjectMapper().writeValueAsString(aCase);
    return this.createRecord(caseJson, "case");
  }

  public CallResult createAccount(final Account account) throws IOException,
                                                               AuthenticationException {
    String accountJson = new ObjectMapper().writeValueAsString(account);
    RecordCreated recordCreated = this.createRecord(accountJson, "account");
    return createCallResult(recordCreated, account);
  }

  public List<Account> createAccounts(List<Account> accounts)
      throws IOException, AuthenticationException {
/*
    Account[] accountsArr = accounts.toArray(new Account[0]);

    Records<Account> records = new Records<>(accountsArr);

    RecordsCreated recordsCreated = createMultipleRecords(records, "account");

    Map<String, String> refIdToId = new HashMap<>();
    for (Attributes attribute : recordsCreated.results) {
      refIdToId.put(attribute.getReferenceId(), attribute.getId());
      System.out.println("Have id " + attribute.getId() + " for the reference id " + attribute.getReferenceId());
    }

    // TODO: If one record failed then have an auto indicator on the record in case the id has
    //  not bee set
    for (Account account : accounts) {
      account.setId(refIdToId.get(account.getAttributes().getReferenceId()));
      System.out.println("Account "+account.toString());
    }*/

    return accounts;
  }

  public void getCasesOfAccount(String accountId) throws IOException, AuthenticationException {
    client.query("SELECT Id FROM Account WHERE Id = \'"+accountId+"\'");
  }

  public void getAllAccounts() throws IOException, AuthenticationException {
    String queryResult = client.query("SELECT Id FROM Account");
    System.out.println(queryResult);
    // TODO: Deserialize query result
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

  public void deleteAllCases() {
    //client.deleteSingleRecord()
  }

  public void deleteAccount(Account account) throws IOException, AuthenticationException {
    String str = client.deleteSingleRecord("account", account.getId());
    System.out.println("Deleted "+str);
  }

  private CallResult createCallResult(RecordCreated recordCreated, Record record) {
    CallResult result = new CallResult();

    if (recordCreated.success) {
      record.setId(recordCreated.id);
      result.successes.add(record);
    } else {
      result.failures.add(record);
    }
    return result;
  }
}
