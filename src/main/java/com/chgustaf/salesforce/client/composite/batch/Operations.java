package com.chgustaf.salesforce.client.composite.batch;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Query;
import com.chgustaf.salesforce.client.composite.domain.Record;
import com.chgustaf.salesforce.client.composite.dto.CompositeBatchResponse;
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
      throws IOException, AuthenticationException, TransactionException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(
        salesforceCompositeBatchClient);

    transaction.create(record);
    if (!transaction.execute()) {
      System.out.println("Unable to create "+record.getClass().getSimpleName()+" record");
      CompositeBatchResponse compositeBatchResponse = transaction.getCompositeBatchResponse();
      System.out.println("Has errors " + compositeBatchResponse.getHasErrors());
      System.out.println(compositeBatchResponse.getResults()[0].getStatusCode());
      System.out.println(compositeBatchResponse.getResults()[0].getResult().textValue());
      //for (Arrays.stream(compositeBatchResponse.getResults()).map( ))
      String exceptionMessage = "Unable to create";
      throw new TransactionException(exceptionMessage);
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

    // TODO Find a different way of returning the record. This is an unchecked return
    return (T) transaction.getRecord(record.getReferenceId(), record.getEntityClass());
  }


  public static <T extends Record> List<T> query(Query<T> query,
                                    SalesforceCompositeBatchClient salesforceCompositeBatchClient)
      throws IOException, AuthenticationException {
    CompositeBatchTransaction transaction = new CompositeBatchTransaction(salesforceCompositeBatchClient);
    transaction.query(query);
    if (!transaction.execute()) {
      return null;
    }
    return transaction.getQueryResult(query.getReferenceId(), query.getEntityClass());
  }
}
