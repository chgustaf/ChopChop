package com.chgustaf.salesforce.client.composite.batch;

import com.chgustaf.examples.caseupdater.client.domain.Account;
import com.chgustaf.salesforce.authentication.exceptions.AuthenticationException;
import com.chgustaf.salesforce.client.SalesforceCompositeBatchClient;
import com.chgustaf.salesforce.client.composite.domain.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsynchronousOperations {

  public static CompletableFuture<List<Account>> queryAsync(Query query,
                                                            SalesforceCompositeBatchClient salesforceCompositeBatchClient) {
     return CompletableFuture.supplyAsync(
        () -> {
          System.out.println("Hello here we go");
          List<Account> accountList = new ArrayList<>();
          try {
            accountList = Operations.query(query, salesforceCompositeBatchClient);

          } catch (IOException e) {
            e.printStackTrace();
          } catch (AuthenticationException e) {
            e.printStackTrace();
          }
          return accountList;
        });
  }
}
