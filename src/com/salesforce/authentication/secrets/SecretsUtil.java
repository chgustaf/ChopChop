package com.salesforce.authentication.secrets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SecretsUtil {

  public static Secrets readCredentials(String fileName) throws FileNotFoundException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile());
    Secrets secrets = readJSON(file);
    return secrets;
  }

  public static Secrets readTestCredentials(String fileName) throws IOException {
    File file = new File("src/test/resources/"+fileName);
    Secrets secrets = readJSON(file);
    return secrets;
  }

  public static Secrets readJSON(File file) throws FileNotFoundException {
    InputStream is = new FileInputStream(file);

    JSONTokener tokener = new JSONTokener(is);
    JSONObject jsonObject = new JSONObject(tokener);
    return new SecretsBuilder()
        .setUsername(jsonObject.getString("username"))
        .setPassword(jsonObject.getString("password"))
        .setConsumerKey(jsonObject.getString("consumer_key"))
        .setConsumerSecret(jsonObject.getString("consumer_secret"))
        .setSecurityToken(jsonObject.getString("security_token"))
        .setLoginUrl(jsonObject.getString("login_url"))
        .createSecrets();
  }
}
