package com.innovationmadness;

import com.salesforce.client.SalesforceHttpClient;

public class Main {

  public static void main(String[] args) {

    try {
      SalesforceHttpClient client = new SalesforceHttpClient(SalesforceHttpClient.AuthenticationFlow.USER_PASSWORD);
      System.out.println("Query "+client.query("SELECT ID FROM Case"));
      Thread.sleep(30000);
      System.out.println("Query "+client.query("SELECT ID FROM Case"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
