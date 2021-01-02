package com.examples.caseupdater.client;

import com.examples.caseupdater.client.composite.CompositeRequest;
import com.examples.caseupdater.client.composite.CompositeRequestBuilder;
import com.examples.caseupdater.client.domain.Record;
import com.examples.caseupdater.client.composite.Request;
import com.examples.caseupdater.client.composite.RequestBuilder;
import com.examples.caseupdater.client.composite.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.exceptions.AuthenticationException;
import com.salesforce.rest.ImprovedSalesforceClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompositeTransaction {

  List<CompositeRequest> compositeRequests;

  ObjectMapper mapper;
  boolean allOrNone;
  ImprovedSalesforceClient improvedSalesforceClient;

  public CompositeTransaction(ImprovedSalesforceClient improvedSalesforceClient) {
    this(improvedSalesforceClient, false);
  }

  public CompositeTransaction(ImprovedSalesforceClient improvedSalesforceClient, boolean allOrNone) {
    compositeRequests = new ArrayList<>();
    mapper = new ObjectMapper();
    this.allOrNone = allOrNone;
    this.improvedSalesforceClient = improvedSalesforceClient;

  }

  public void update(Record record) throws JsonProcessingException {
    CompositeRequest compositeRequest =
        new CompositeRequestBuilder()
            .setMethod("PATCH")
            .setBody(mapper.writeValueAsString(record))
            .setUrl("/services/data/v50.0/sobjects/"+record.getSObjectName()+"/"+record.getId())
            .createCompositeRequest();
    compositeRequests.add(compositeRequest);
  }

  public void create(Record record) throws JsonProcessingException {
    CompositeRequest compositeRequest =
        new CompositeRequestBuilder()
            .setMethod("POST")
            .setBody(mapper.writeValueAsString(record))
            .setUrl("/services/data/v50.0/sobjects/"+record.getSObjectName())
            .createCompositeRequest();
    compositeRequests.add(compositeRequest);
  }

  public void delete(Record record) {
    CompositeRequest compositeRequest =
        new CompositeRequestBuilder()
            .setMethod("DELETE")
            .setUrl("/services/data/v50.0/sobjects/"+record.getSObjectName()+"/"+record.getId())
            .createCompositeRequest();
    compositeRequests.add(compositeRequest);
  }

  public void get(Record record) {
    //TODO: fix so we can read what fields we have in the record classes
    CompositeRequest compositeRequest =
        new CompositeRequestBuilder()
            .setMethod("GET")
            .setUrl("/services/data/v50.0/sobjects/"+record.getSObjectName()+"/"+record.getId())
            .createCompositeRequest();
    compositeRequests.add(compositeRequest);
  }

  public void update(List<Record> records) throws JsonProcessingException {
    for (Record record : records) {
      update(record);
    }
  }

  public void create(List<Record> records) throws JsonProcessingException {
    for (Record record : records) {
      create(record);
    }
  }

  public void delete(List<Record> records) {
    for (Record record : records) {
      delete(record);
    }
  }

  public Response execute() throws IOException, AuthenticationException {
    String payload = renderPayload();
    String responseString = improvedSalesforceClient.compositeCall(payload);
    System.out.println(responseString);
    return mapper.readValue(responseString, Response.class);
  }

  public String renderPayload() throws JsonProcessingException {
    CompositeRequest[] compositeRequestsArray =
        compositeRequests.toArray(new CompositeRequest[compositeRequests.size()]);
    Request request =
        new RequestBuilder()
            .setCompositeRequest(compositeRequestsArray)
            .setAllOrNone(false)
            .createRequest();
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(request);
  }

  //TODO: Create a class that looks at the request and/or the response and figures out what
  // should be
  // returned. If it is a get then there should be an SObject returned.
  // List all the different kinds of responses
}
