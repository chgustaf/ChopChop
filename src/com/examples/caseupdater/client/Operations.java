package com.examples.caseupdater.client;

import com.examples.caseupdater.client.domain.Account;
import com.examples.caseupdater.client.domain.Query;
import com.examples.caseupdater.client.domain.Record;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.SalesforceCompositeBatchClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Operations {




  public static <T extends Record> List<T> createRecords(List<T> records,
                                                         SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);
    for (T record : records) {
      transaction.create(record);
    }
    if (!transaction.execute()) {
      System.out.println("Unable to create Account");
      return null;
    }

    List<T> returnList = new ArrayList<>();
    for (T record : records) {
      returnList.add((T)transaction.getRecord(record.getReferenceId(), record.getEntityClass()));
    }
    return returnList;
  }

  public static <T extends Record> T get(T record,
                                             SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);

    transaction.get(record);
    if (!transaction.execute()) {
      System.out.println("Unable to create "+record.getClass().getSimpleName()+" record");
      return null;
    }

    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }

  public static <T extends Record> T create(T record,
                                             SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);

    transaction.create(record);
    if (!transaction.execute()) {
      System.out.println("Unable to create "+record.getClass().getSimpleName()+" record");
      return null;
    }

    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }

  public static <T extends Record> T update(T record,
                                             SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);

    transaction.update(record);
    if (!transaction.execute()) {
      System.out.println("Unable to create "+record.getClass().getSimpleName()+" record");
      return null;
    }

    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }

  public static <T extends Record> T delete(T record,
                                             SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);

    transaction.delete(record);
    if (!transaction.execute()) {
      System.out.println("Unable to create "+record.getClass().getSimpleName()+" record");
      return null;
    }

    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }

  public static List<Account> query(Query query,
                                    SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(salesforceCompositeBatchClient);
    transaction.query(query);
    if (!transaction.execute()) {
      System.out.println("Unable to query");
      return null;
    }
    return transaction.getQueryResult(query.getReferenceId(), Account.class);
  }
}
