package com.salesforce.rest;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.salesforce.exceptions.AuthenticationException;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImprovedSalesforceClient {

  SalesforceClient client;

  public Integer apiVersion = 50;

  public String instanceUrl;
  public String baseEndpoint;
  public String compositeSobjectEndpoint;
  public String readEndpoint;

  public ImprovedSalesforceClient(SalesforceClient client) throws IOException,
                                                                      AuthenticationException {
    this.client = client;
    initEndpoints();
  }

  private void initEndpoints() {
    instanceUrl = client.accessParameters.instanceUrl;
    baseEndpoint = instanceUrl+"/services/data/v"+apiVersion+".0/";
    compositeSobjectEndpoint = baseEndpoint+"composite/sobjects";
    readEndpoint = instanceUrl + baseEndpoint;
  }

  public String read(List<String> ids, List<String> fields, String objectName)
      throws IOException, AuthenticationException {
    String idsCsv = String.join(",", ids);
    String fieldsCsv = String.join(",", fields);
    HttpGet getRequest = new HttpGet(compositeSobjectEndpoint + "/" + objectName+ "?ids=" + idsCsv+
                                     "&fields="+fieldsCsv);
    getRequest.addHeader("accept", "application/json");
    getRequest.addHeader("Authorization", "Bearer " + client.accessParameters.accessToken);
    return client.executeHttpRequest(getRequest);
  }

  public String create(List<String> records, boolean allOrNone)
      throws IOException, AuthenticationException {
    System.out.println("Records received "+records);
    HttpPost postRequest = new HttpPost(compositeSobjectEndpoint);
    postRequest.addHeader("Content-Type", "application/json");
    postRequest.addHeader("Authorization", "Bearer " + client.accessParameters.accessToken);
    postRequest.setEntity(new StringEntity(createPostPayload(records, allOrNone), UTF_8));
    return client.executeHttpRequest(postRequest);
  }

  /**
   * Takes individual record ids and deletes the records
   *
   * @param ids Individual record ids
   * @param allOrNone
   * @return
   * @throws IOException
   * @throws AuthenticationException
   */
  public String delete(List<String> ids, boolean allOrNone)
      throws IOException, AuthenticationException {
    String idsCsv = String.join(",", ids);
    HttpDelete deleteRequest = new HttpDelete(compositeSobjectEndpoint + "?ids=" + idsCsv +
                                              "&allOrNone="+allOrNone);
    deleteRequest.addHeader("Authorization", "Bearer " + client.accessParameters.accessToken);
    return client.executeHttpRequest(deleteRequest);
  }

  /**
   * Records containing attributes, id and the fields that should be updated.
   * E.g.
   * {"attributes": {"type": "Account"},
   * "id": "001whatever",
   * "NumberOfEmployees": 2700}
   *
   * @param
   * @return
   */
  public String update(List<String> records, boolean allOrNone) {
    // for each check if the
    // Check if contains attributes

    // Check if contains id
    HttpPatch patchRequest = new HttpPatch(compositeSobjectEndpoint);
    patchRequest.addHeader("Content-Type", "application/json");
    patchRequest.addHeader("Authorization", "Bearer " + client.accessParameters.accessToken);
   // patchRequest.setEntity(new StringEntity());
    return "";
  }

  private String createPostPayload(List<String> records, boolean allOrNone) {
    JSONObject json = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    records.forEach(str -> jsonArray.put(new JSONObject(str)));
    json.put("records", jsonArray);
    return json.toString().replace("\\","");
  }

  private String createPatchPayload(List<String> records, boolean allOrNone) {
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    return "";
  }

}
