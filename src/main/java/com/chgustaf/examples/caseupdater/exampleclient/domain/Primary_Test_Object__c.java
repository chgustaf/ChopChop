package com.chgustaf.examples.caseupdater.exampleclient.domain;

import com.chgustaf.salesforce.client.composite.domain.Record;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "Primary_Test_Object__c")
public class Primary_Test_Object__c extends Record {

  private Boolean testCheckbox;
  private Float testCurrency;
  private Date testDate;
  private ZonedDateTime testDateTime;
  private String testEmail;
  private String testFormulaField;
  private double testGeolocationLatitude;
  private double testGeolocationLongitude;
  private String testMultiSelectPicklist;
  private String testName;
  private Integer testNumber;
  private Float testPercentage;
  private String testPhone;
  private String testPicklist;
  private String testText;
  private String testTextArea;
  private String testTextAreaLong;
  private String testTextAreaRich;
  private String testTextEncrypted;
  private Instant testTime;
  private String testUrl;

  public Primary_Test_Object__c() {
    super(Primary_Test_Object__c.class);
  }

  @JsonProperty("Test_Checkbox__c")
  public Boolean getTestCheckbox() {
    return testCheckbox;
  }

  public void setTestCheckbox(final Boolean testCheckbox) {
    this.testCheckbox = testCheckbox;
  }

  public Float getTestCurrency() {
    return testCurrency;
  }

  @JsonProperty("Test_Currency__c")
  public void setTestCurrency(final Float testCurrency) {
    this.testCurrency = testCurrency;
  }

  public Date getTestDate() {
    return testDate;
  }

  @JsonProperty("Test_Date__c")
  public void setTestDate(final Date testDate) {
    this.testDate = testDate;
  }
/*
  public ZonedDateTime getTestDateTime() {
    return testDateTime;
  }

  @JsonProperty("Test_Datetime__c")
  public void setTestDateTime(final ZonedDateTime testDateTime) {
    this.testDateTime = testDateTime;
  }*/

  @JsonProperty("Test_Email__c")
  public String getTestEmail() {
    return testEmail;
  }

  public void setTestEmail(final String testEmail) {
    this.testEmail = testEmail;
  }

  public String getTestFormulaField() {
    return testFormulaField;
  }

  @JsonProperty("Test_Formula_Field__c")
  public void setTestFormulaField(final String testFormulaField) {
    this.testFormulaField = testFormulaField;
  }

  @JsonProperty("Test_Geolocation__latitude__s")
  public double getTestGeolocationLatitude() {
    return testGeolocationLatitude;
  }

  @JsonProperty("Test_Geolocation__latitude__s")
  public void setTestGeolocationLatitude(final double testGeolocationLatitude) {
    this.testGeolocationLatitude = testGeolocationLatitude;
  }

  @JsonProperty("Test_Geolocation__longitude__s")
  public double getTestGeolocationLongitude() {
    return testGeolocationLongitude;
  }

  @JsonProperty("Test_Geolocation__longitude__s")
  public void setTestGeolocationLongitude(final double testGeolocationLongitude) {
    this.testGeolocationLongitude = testGeolocationLongitude;
  }

  public String getTestMultiSelectPicklist() {
    return testMultiSelectPicklist;
  }

  @JsonProperty("Test_Multiselect_Picklist__c")
  public void setTestMultiSelectPicklist(final String testMultiSelectPicklist) {
    this.testMultiSelectPicklist = testMultiSelectPicklist;
  }

  public String getTestName() {
    return testName;
  }

  public Integer getTestNumber() {
    return testNumber;
  }

  @JsonProperty("Test_Number__c")
  public void setTestNumber(final Integer testNumber) {
    this.testNumber = testNumber;
  }

  public Float getTestPercentage() {
    return testPercentage;
  }

  @JsonProperty("Test_Percentage__c")
  public void setTestPercentage(final Float testPercentage) {
    this.testPercentage = testPercentage;
  }

  public String getTestPhone() {
    return testPhone;
  }

  @JsonProperty("Test_Phone__c")
  public void setTestPhone(final String testPhone) {
    this.testPhone = testPhone;
  }

  public String getTestPicklist() {
    return testPicklist;
  }

  @JsonProperty("Test_Picklist__c")
  public void setTestPicklist(final String testPicklist) {
    this.testPicklist = testPicklist;
  }

  public String getTestText() {
    return testText;
  }

  @JsonProperty("Test_Text__c")
  public void setTestText(final String testText) {
    this.testText = testText;
  }

  public String getTestTextArea() {
    return testTextAreaLong;
  }

  @JsonProperty("Test_Text_Area__c")
  public void setTestTextArea(final String testTextArea) {
    this.testTextArea = testTextArea;
  }

  public String getTestTextAreaLong() {
    return testTextAreaLong;
  }

  @JsonProperty("Test_Text_Area_Long__c")
  public void setTestTextAreaLong(final String testTextAreaLong) {
    this.testTextAreaLong = testTextAreaLong;
  }

  public String getTestTextAreaRich() {
    return testTextAreaRich;
  }

  @JsonProperty("Test_Text_Area_Rich__c")
  public void setTestTextAreaRich(final String testTextAreaRich) {
    this.testTextAreaRich = testTextAreaRich;
  }

  public String getTestTextEncrypted() {
    return testTextEncrypted;
  }

  @JsonProperty("Test_Text_Encrypted__c")
  public void setTestTextEncrypted(final String testTextEncrypted) {
    this.testTextEncrypted = testTextEncrypted;
  }

  public Instant getTestTime() {
    return testTime;
  }

  @JsonProperty("Test_Time__c")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  public void setTestTime(final Instant testTime) {
    this.testTime = testTime;
  }

  public String getTestUrl() {
    return testUrl;
  }

  @JsonProperty("Test_URL__c")
  public void setTestUrl(final String testUrl) {
    this.testUrl = testUrl;
  }
}
