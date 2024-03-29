package com.chgustaf.salesforce.client.composite.batch;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Query;
import com.chgustaf.salesforce.client.composite.domain.QueryResult;
import com.chgustaf.salesforce.client.composite.domain.Record;
import com.chgustaf.salesforce.client.composite.domain.TransactionError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Operations {

  public static <T extends Record> List<T> create(List<T> records,
                                                  SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    for (T record : records) {
      transaction.create(record);
    }
    if (!transaction.execute()) {
      throw buildTransactionException(records, transaction);
    }
    return retrieveResults(records, transaction);
  }

  public static <T extends Record> List<T> get(List<T> records,
                                               SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    for (T record : records) {
      transaction.get(record);
    }
    if (!transaction.execute()) {
      throw buildTransactionException(records, transaction);
    }
    return retrieveResults(records, transaction);
  }

  public static <T extends Record> List<T> update(List<T> records,
                                                  SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    for (T record : records) {
      transaction.update(record);
    }
    if (!transaction.execute()) {
      throw buildTransactionException(records, transaction);
    }

    return retrieveResults(records, transaction);
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

  public static <T extends Record> List<T> delete(List<T> records,
                                                  SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction =
        new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
    for (T record : records) {
      transaction.delete(record);
    }
    if (!transaction.execute()) {
      throw buildTransactionException(records, transaction);
    }
    return retrieveResults(records, transaction);
  }


  public static <T extends Record> List<T> query(Query<T> query,
                                                 SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException, TransactionException {
    boolean thereAreMoreRecords = true;
    CompositeBatchTransaction transaction;
    List<T> returnList = new ArrayList<>();
    Query<T> currentQuery = query;
    String nextUrl = null;
    do {
      if (nextUrl != null) {
        currentQuery = new Query<>(query.getEntityClass());
        currentQuery.setUrl(nextUrl);
      }
      transaction = new CompositeBatchTransaction(salesforceCompositeBatchClient, false);
      transaction.query(currentQuery);

      if (!transaction.execute()) {
        QueryResult queryResult = transaction.getQueryResult(currentQuery.getReferenceId());
        if (queryResult.errors != null && !queryResult.errors.isEmpty()) {
          String exceptionMessage =
              queryResult.errors.stream().map(err -> err.getErrorCode() + " " + err.getMessage()).collect(
              Collectors.joining(", "));
          throw new TransactionException(exceptionMessage,
              queryResult.errors.stream().findFirst().map(TransactionError::getErrorCode).orElse(
                  "UNKNOWN_ERROR"));
        } else {
          String exceptionMessage = "Unknown exception";
          throw new TransactionException(exceptionMessage, "UNKNOWN_ERROR");
        }
      }

      //TODO: continue here. Should we move the query exctraction code into the QureyResult class?
      returnList.addAll(transaction.getQueryResultList(currentQuery.getReferenceId(),
          query.getEntityClass()));

      if (!transaction.done(currentQuery.getReferenceId())) {
        nextUrl = transaction.nextRecordsUrl(currentQuery.getReferenceId());
      } else {
        thereAreMoreRecords = false;
      }
    } while (thereAreMoreRecords);
    return returnList;
  }

  private static <T extends Record>  List<TransactionError> collectTransactionErrors(
      List<T> records,
      CompositeBatchTransaction transaction) throws IOException {
    List<TransactionError> errors = new ArrayList<>();
    for (T record : records) {
      errors.addAll(transaction.getRecord(record.getReferenceId(),
          record.getEntityClass()).getErrors());
    }
    return errors;
  }

  private static TransactionException constructTransactionException(
      List<TransactionError> transactionErrors) {
    String errorMessage = "";
    for (TransactionError transactionError : transactionErrors) {
      errorMessage += transactionError.getErrorCode()+ " - " + transactionError.getMessage();
    }
    String code = (transactionErrors.size() > 1) ? "MULTIPLE_ERRORS" :
                  transactionErrors.get(0).getErrorCode();
    return new TransactionException(errorMessage, code);
  }

  private static <T extends Record> TransactionException buildTransactionException(List<T> records, CompositeBatchTransaction transaction)
      throws IOException, TransactionException {
    throw constructTransactionException(collectTransactionErrors(records, transaction));
  }

  private static <T extends Record> List<T> retrieveResults(List<T> records,
                                                            CompositeBatchTransaction transaction)
      throws IOException {
    List<T> returnList = new ArrayList<>();
    for (T record : records) {
      returnList.add((T)transaction.getRecord(record.getReferenceId(), record.getEntityClass()));
    }
    return returnList;
  }
}
