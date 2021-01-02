package com.examples.caseupdater.client.composite.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;

public class GenericBatchRequest<T> {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String binaryPartName;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String getBinaryPartNameAlias;

  String method; // required

  @JsonRawValue String richInput;

  String url; // required
  BatchRequest.Type type;
  String sobjectName;
  String referenceId;
}