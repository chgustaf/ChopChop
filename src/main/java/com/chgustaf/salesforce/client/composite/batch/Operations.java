package com.chgustaf.salesforce.client.composite.batch;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Query;
import com.chgustaf.salesforce.client.composite.domain.Record;
import com.chgustaf.salesforce.client.composite.domain.TransactionError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Operations {

  public static <T extends Record> List<T> createRecords(List<T> records,
                                                         SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
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
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);

    transaction.get(record);
    if (!transaction.execute()) {
      Record returnRecord = transaction.getRecord(record.getReferenceId(), record.getEntityClass());
      throw constructTransactionException(returnRecord.getErrors());
    }

    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }

  public static <T extends Record> T create(T record,
                                             SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);

    transaction.create(record);
    if (!transaction.execute()) {
      Record returnRecord = transaction.getRecord(record.getReferenceId(), record.getEntityClass());
      throw constructTransactionException(returnRecord.getErrors());
    }
    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }

  public static <T extends Record> T update(T record,
                                             SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);

    transaction.update(record);
    if (!transaction.execute()) {
      Record returnRecord = transaction.getRecord(record.getReferenceId(), record.getEntityClass());
      throw constructTransactionException(returnRecord.getErrors());
    }

    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }

  public static <T extends Record> T delete(T record,
                                             SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient, false);

    transaction.delete(record);
    if (!transaction.execute()) {
      Record returnRecord = transaction.getRecord(record.getReferenceId(), record.getEntityClass());
      throw constructTransactionException(returnRecord.getErrors());
    }
    // TODO Find a different way of returning the record. This is an unchecked return
    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }


  public static <T extends Record> List<T> query(Query<T> query,
                                    SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    transaction.query(query);
    if (!transaction.execute()) {
      Record returnRecord = transaction.getRecord(query.getReferenceId(), query.getEntityClass());
      throw constructTransactionException(returnRecord.getErrors());
    }
    List<T> recordList = transaction.getQueryResult(query.getReferenceId(), query.getEntityClass());
    String nextUrl = transaction.hasMore(query.getReferenceId());
    while (nextUrl != null) {
      Query nextQuery = new Query<>(query.getEntityClass());
      nextQuery.setUrl(nextUrl);
      transaction = new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
      transaction.query(nextQuery);
      if (!transaction.execute()) {
        Record returnRecord = transaction.getRecord(query.getReferenceId(), query.getEntityClass());
        throw constructTransactionException(returnRecord.getErrors());
      }
      recordList.addAll(transaction.getQueryResult(nextQuery.getReferenceId(), query.getEntityClass()));
      nextUrl = transaction.hasMore(nextQuery.getReferenceId());
    }
    System.out.println("Here are all records " + recordList.size());
    return recordList;
  }

  private static TransactionException constructTransactionException(
      List<TransactionError> transactionErrors) {
    String errorMessage = "";
    for (TransactionError transactionError : transactionErrors) {
      errorMessage += transactionError.getErrorCode()+ " - " + transactionError.getMessage();
    }
    return new TransactionException(errorMessage);
  }
}
