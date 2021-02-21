package com.chgustaf.salesforce.client.composite.domain;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class QueryResult {
  public int totalSize;
  public boolean done;
  public JsonNode records;
  public String nextRecordsUrl;
  public List<TransactionError> errors;
  public Boolean success;
}
