package com.chgustaf.salesforce.authentication.secrets;

import java.security.Key;
import java.security.interfaces.RSAPublicKey;

public class Secrets {

    String username;
    String password;
    String securityToken;
    String clientId;
    String clientSecret;
    String loginUrl;
    String tokenUrl;
    Integer apiVersion;
    String authenticationMethod;
    String jksFileName;
    String jksPassword;
    String jksKeyname;

    Key privateKey;
    RSAPublicKey publicKey;

    Secrets(final String username, final String password, final String securityToken,
            final String clientId,
            final String clientSecret, final String loginUrl, final String tokenUrl,
            final Integer apiVersion, final String authenticationMethod, final String jksFileName,
            final String jksPassword, final String jksKeyname) {
        this.username = username;
        this.password = password;
        this.securityToken = securityToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.loginUrl = loginUrl;
        this.tokenUrl = tokenUrl;
        this.apiVersion = apiVersion;
        this.authenticationMethod = authenticationMethod;
        this.jksFileName = jksFileName;
        this.jksPassword = jksPassword;
        this.jksKeyname = jksKeyname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(final String securityToken) {
        this.securityToken = securityToken;
    }

    public String getConsumerKey() {
        return clientId;
    }

    public void setConsumerKey(final String clientId) {
        this.clientId = clientId;
    }

    public String getConsumerSecret() {
        return clientSecret;
    }

    public void setConsumerSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Key getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(final Key privateKey) {
        this.privateKey = privateKey;
    }

    private RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(final String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public Integer getApiVersion() {
        return apiVersion;
    }

    private void setApiVersion(final Integer apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    public String getJksFileName() {
        return jksFileName;
    }

    public String getJksPassword() {
        return jksPassword;
    }

    public void setJksKeyname(final String jksKeyname) {
        this.jksKeyname = jksKeyname;
    }

    public String getJksKeyname() {
        return jksKeyname;
    }
}
