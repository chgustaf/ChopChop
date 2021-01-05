package com.examples.caseupdater.client.domain;

import com.fasterxml.jackson.databind.JsonNode;

public class QueryResult {

  public int totalSize;
  public boolean done;
  public JsonNode records;
}
