package com.examples.caseupdater.client.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface Record {

  Boolean getSuccess();

  String getSObjectName();

  String getReferenceId();

  String getId();

  String getJSON() throws JsonProcessingException;

  Integer getStatusCode();

  void setSuccess(Boolean success);

  void setReferenceId(String referenceId);

  void setId(String id);

  void setJSON(String json);

  void setStatusCode(Integer statusCode);

  List<String> getAllFields();

}
