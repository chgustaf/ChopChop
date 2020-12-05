package com.innovationmadness;

import com.salesforce.rest.SalesforceClient;

public class Main {

  public static void main(String[] args) {

    try {
      SalesforceClient client = new SalesforceClient(SalesforceClient.AuthenticationFlow.USER_PASSWORD);
      System.out.println("Query "+client.query("SELECT ID FROM Case"));
      Thread.sleep(30000);
      System.out.println("Query "+client.query("SELECT ID FROM Case"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
