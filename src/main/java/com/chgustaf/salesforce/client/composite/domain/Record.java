package com.chgustaf.salesforce.client.composite.domain;

import com.chgustaf.salesforce.client.composite.dto.Attributes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Record<T> {

  protected String id;
  protected Integer statusCode;
  protected Attributes attributes;
  @JsonIgnore
  protected ObjectMapper mapper;
  protected String sobjectName;
  protected Boolean success;
  protected List<TransactionError> errors;
  private Class<T> entityClass;


  protected Record(Class<T> entityClass) {
    this.entityClass = entityClass;
    this.sobjectName = entityClass.getSimpleName();
    this.attributes = new Attributes("Account", UUID.randomUUID().toString());
    this.errors = new ArrayList<>();
    this.mapper = new ObjectMapper();
  }

  @JsonIgnore
  public String getId() {
    return id;
  }

  @JsonIgnore
  public Class<T> getEntityClass() {
    return entityClass;
  }

  @JsonSetter("Id")
  public void setId(final String id) {
    this.id = id;
  }

  @JsonIgnore
  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final Integer statusCode) {
    this.statusCode = statusCode;
  }

  @JsonIgnore
  public void setErrors(final List<TransactionError> errors) {
    this.errors = errors;
  }

  @JsonIgnore
  public List<TransactionError> getErrors() {
    return errors;
  }

  @JsonIgnore
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(final Boolean success) {
    this.success = success;
  }

  public void setReferenceId(final String referenceId) {
    this.attributes.setReferenceId(referenceId);
  }

  @JsonIgnore
  public String getReferenceId() {
    return attributes.getReferenceId();
  }


  @JsonIgnore
  public Attributes getAttributes() {
    return attributes;
  }


  public void setAttributes(final Attributes attributes) {
    this.attributes = attributes;
  }

  @JsonIgnore
  public String getSObjectName() {
    return this.getClass().getSimpleName();
  }

  public void setSobjectName(final String sobjectName) {
    this.sobjectName = sobjectName;
  }

  public Map<String, String> getFieldNameToJsonPropertyMap(Class clazz) {
    Map<String, String> returnMap = new HashMap<>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(JsonProperty.class)) {
        String annotationValue = field.getAnnotation(JsonProperty.class).value();
        returnMap.put(field.getName(),annotationValue);
      }
    }
    return returnMap;
  }

  @JsonIgnore
  protected List<String> getAllFieldsHelper(Class clazz) {
    Map<String, String> fieldNameToJsonPropertyMap = getFieldNameToJsonPropertyMap(clazz);
    return Arrays.stream(clazz.getDeclaredFields())
        .map(field -> fieldNameToJsonPropertyMap.get(field.getName()))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public List<String> getAllFields() {
    return getAllFieldsHelper(entityClass);
  }

  @JsonIgnore
  public String getJSON() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new JavaTimeModule());
    return mapper.writeValueAsString(this);
  }

}
