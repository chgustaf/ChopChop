package com.salesforce.authentication.secrets;

public class SecretsBuilder {

  private String username;
  private String password;
  private String securityToken;
  private String consumerKey;
  private String consumerSecret;
  private String loginUrl;
  private String tokenUrl;
  private Integer apiVersion;

  public SecretsBuilder setUsername(final String username) {
    this.username = username;
    return this;
  }

  public SecretsBuilder setPassword(final String password) {
    this.password = password;
    return this;
  }

  public SecretsBuilder setSecurityToken(final String securityToken) {
    this.securityToken = securityToken;
    return this;
  }

  public SecretsBuilder setConsumerKey(final String consumerKey) {
    this.consumerKey = consumerKey;
    return this;
  }

  public SecretsBuilder setConsumerSecret(final String consumerSecret) {
    this.consumerSecret = consumerSecret;
    return this;
  }

  public SecretsBuilder setLoginUrl(final String loginUrl) {
    this.loginUrl = loginUrl;
    return this;
  }

  public SecretsBuilder setTokenUrl(final String tokenUrl) {
    this.tokenUrl = tokenUrl;
    return this;
  }

  public SecretsBuilder setApiVersion(final Integer apiVersion) {
    this.apiVersion = apiVersion;
    return this;
  }

  public Secrets createSecrets() {
    return new Secrets(username, password, securityToken, consumerKey, consumerSecret, loginUrl,
        tokenUrl, apiVersion);
  }
}