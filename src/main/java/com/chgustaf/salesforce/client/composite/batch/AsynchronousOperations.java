package com.chgustaf.salesforce.client.composite.batch;

import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.authentication.exceptions.TransactionException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Query;
import com.chgustaf.salesforce.client.composite.domain.Record;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsynchronousOperations {

  public static <T extends Record> CompletableFuture<List<T>> queryAsync(Query<T> query,
                                                                        SalesforceCompositeBatchClient salesforceCompositeBatchClient) {
    return CompletableFuture.supplyAsync(
        () -> {
          System.out.println("Hello here we go");
          List<T> recordList = new ArrayList<>();
          try {
            recordList = Operations.query(query, salesforceCompositeBatchClient);
            System.out.println("Here are the records "+recordList);
          } catch (IOException | AuthenticationException | TransactionException e) {
            e.printStackTrace();
          }
          return recordList;
        });
  }
}