package com.chgustaf.examples.caseupdater.exampleclient.domain;

import com.chgustaf.salesforce.client.composite.domain.Record;
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
  private String testGeolocation;
  private String testMultiSelectPicklist;
  private String testName;
  private String testNumber;
  private Float testPercentage;
  private String testPhone;
  private String testPicklist;
  private String testText;
  private String testTextAreaLong;
  private String testTextAreaRich;
  private String testTextEncrypted;
  private Instant testTime;
  private String url;

  public Primary_Test_Object__c() {
    super(Primary_Test_Object__c.class);
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

  @JsonProperty("Test_Eail__c")
  public String getTestEmail() {
    return testEmail;
  }

  @JsonProperty("Test_Eail__c")
  public void setTestEmail(final String testEmail) {
    this.testEmail = testEmail;
  }

  public String getTestFormulaField() {
    return testFormulaField;
  }

  public void setTestFormulaField(final String testFormulaField) {
    this.testFormulaField = testFormulaField;
  }

  public String getTestGeolocation() {
    return testGeolocation;
  }

  public void setTestGeolocation(final String testGeolocation) {
    this.testGeolocation = testGeolocation;
  }

  public String getTestMultiSelectPicklist() {
    return testMultiSelectPicklist;
  }

  public void setTestMultiSelectPicklist(final String testMultiSelectPicklist) {
    this.testMultiSelectPicklist = testMultiSelectPicklist;
  }

  public String getTestName() {
    return testName;
  }

  public void setTestName(final String testName) {
    this.testName = testName;
  }

  public String getTestNumber() {
    return testNumber;
  }

  public void setTestNumber(final String testNumber) {
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

  public Instant getTestTime() {
    return testTime;
  }

  public void setTestTime(final Instant testTime) {
    this.testTime = testTime;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }
}
