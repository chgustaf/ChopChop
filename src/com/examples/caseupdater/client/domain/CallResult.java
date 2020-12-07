package com.examples.caseupdater.client.domain;

import java.util.ArrayList;
import java.util.List;

public class CallResult {

  public List<Record> successes;

  public List<Record> failures;

  public CallResult() {
    successes = new ArrayList<>();
    failures = new ArrayList<>();
  }
}
