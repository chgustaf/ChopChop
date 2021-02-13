package com.chgustaf.salesforce.client.composite.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.ZonedDateTime;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "Primary_Test_Object__c")
public class Primary_Test_Object__c extends Record {

  @JsonProperty("Test_Checkbox__c")
  private Boolean testCheckbox;
  @JsonProperty("Test_Currency__c")
  private Float testCurrency;
  @JsonProperty("Test_Date__c")
  private Date testDate;
  @JsonProperty("Test_Datetime__c")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private ZonedDateTime testDateTime;
  @JsonProperty("Test_Email__c")
  private String testEmail;
  @JsonProperty("Test_Formula_Field__c")
  private String testFormulaField;
  @JsonProperty("Test_Geolocation__latitude__s")
  private double testGeolocationLatitude;
  @JsonProperty("Test_Geolocation__longitude__s")
  private double testGeolocationLongitude;
  @JsonProperty("Test_Multiselect_Picklist__c")
  private String testMultiSelectPicklist;
  @JsonProperty("Name")
  private String testName;
  @JsonProperty("Test_Number__c")
  private Integer testNumber;
  @JsonProperty("Test_Percentage__c")
  private Float testPercentage;
  @JsonProperty("Test_Phone__c")
  private String testPhone;
  @JsonProperty("Test_Picklist__c")
  private String testPicklist;
  @JsonProperty("Test_Text__c")
  private String testText;
  @JsonProperty("Test_Text_Area__c")
  private String testTextArea;
  @JsonProperty("Test_Text_Area_Long__c")
  private String testTextAreaLong;
  @JsonProperty("Test_Text_Area_Rich__c")
  private String testTextAreaRich;
  @JsonProperty("Test_Text_Encrypted__c")
  private String testTextEncrypted;
  @JsonProperty("Test_Time__c")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  private String testTime;
  @JsonProperty("Test_URL__c")
  private String testUrl;

  public Primary_Test_Object__c() {
    super(Primary_Test_Object__c.class);
  }

  private void setName(final String testName) {
    this.testName = testName;
  }

  public String getTestName() {
    return testName;
  }

  public Boolean getTestCheckbox() {
    return testCheckbox;
  }

  public void setTestCheckbox(final Boolean testCheckbox) {
    this.testCheckbox = testCheckbox;
  }

  public Float getTestCurrency() {
    return testCurrency;
  }


  public void setTestCurrency(final Float testCurrency) {
    this.testCurrency = testCurrency;
  }

  public Date getTestDate() {
    return testDate;
  }


  public void setTestDate(final Date testDate) {
    this.testDate = testDate;
  }

  public ZonedDateTime getTestDateTime() {
    return testDateTime;
  }


  public void setTestDateTime(final ZonedDateTime testDateTime) {
    this.testDateTime = testDateTime;
  }


  public String getTestEmail() {
    return testEmail;
  }

  public void setTestEmail(final String testEmail) {
    this.testEmail = testEmail;
  }

  public String getTestFormulaField() {
    return testFormulaField;
  }


  public void setTestFormulaField(final String testFormulaField) {
    this.testFormulaField = testFormulaField;
  }


  public double getTestGeolocationLatitude() {
    return testGeolocationLatitude;
  }


  public void setTestGeolocationLatitude(final double testGeolocationLatitude) {
    this.testGeolocationLatitude = testGeolocationLatitude;
  }


  public double getTestGeolocationLongitude() {
    return testGeolocationLongitude;
  }

  public void setTestGeolocationLongitude(final double testGeolocationLongitude) {
    this.testGeolocationLongitude = testGeolocationLongitude;
  }

  public String getTestMultiSelectPicklist() {
    return testMultiSelectPicklist;
  }


  public void setTestMultiSelectPicklist(final String testMultiSelectPicklist) {
    this.testMultiSelectPicklist = testMultiSelectPicklist;
  }

  public Integer getTestNumber() {
    return testNumber;
  }


  public void setTestNumber(final Integer testNumber) {
    this.testNumber = testNumber;
  }

  public Float getTestPercentage() {
    return testPercentage;
  }


  public void setTestPercentage(final Float testPercentage) {
    this.testPercentage = testPercentage;
  }

  public String getTestPhone() {
    return testPhone;
  }


  public void setTestPhone(final String testPhone) {
    this.testPhone = testPhone;
  }

  public String getTestPicklist() {
    return testPicklist;
  }


  public void setTestPicklist(final String testPicklist) {
    this.testPicklist = testPicklist;
  }

  public String getTestText() {
    return testText;
  }


  public void setTestText(final String testText) {
    this.testText = testText;
  }

  public String getTestTextArea() {
    return testTextAreaLong;
  }


  public void setTestTextArea(final String testTextArea) {
    this.testTextArea = testTextArea;
  }

  public String getTestTextAreaLong() {
    return testTextAreaLong;
  }


  public void setTestTextAreaLong(final String testTextAreaLong) {
    this.testTextAreaLong = testTextAreaLong;
  }

  public String getTestTextAreaRich() {
    return testTextAreaRich;
  }


  public void setTestTextAreaRich(final String testTextAreaRich) {
    this.testTextAreaRich = testTextAreaRich;
  }

  public String getTestTextEncrypted() {
    return testTextEncrypted;
  }


  public void setTestTextEncrypted(final String testTextEncrypted) {
    this.testTextEncrypted = testTextEncrypted;
  }

  public String getTestTime() {
    return testTime;
  }


  public void setTestTime(final String testTime) {
    this.testTime = testTime;
  }

  public String getTestUrl() {
    return testUrl;
  }

  public void setTestUrl(final String testUrl) {
    this.testUrl = testUrl;
  }
}
