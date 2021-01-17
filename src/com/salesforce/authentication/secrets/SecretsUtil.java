package com.salesforce.authentication.secrets;

import static com.salesforce.authentication.exceptions.AuthenticationException.Code.INVALID_CREDENTIALS;

import com.salesforce.authentication.exceptions.AuthenticationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SecretsUtil {

  public static Secrets readCredentials(String fileName)
      throws FileNotFoundException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile());
    Secrets secrets = readJSON(file);
    return secrets;
  }

  public static Secrets readTestCredentials(String fileName)
      throws IOException {
    File file = new File("src/test/resources/"+fileName);
    Secrets secrets = readJSON(file);
    return secrets;
  }

  public static Secrets readJSON(File file) throws FileNotFoundException {
    InputStream is = new FileInputStream(file);

    JSONTokener tokener = new JSONTokener(is);
    JSONObject jsonObject = new JSONObject(tokener);
    SecretsBuilder secretsBuilder = new SecretsBuilder()
        .setUsername(jsonObject.getString("username"))
        .setPassword(jsonObject.getString("password"))
        .setConsumerKey(jsonObject.getString("consumer_key"))
        .setConsumerSecret(jsonObject.getString("consumer_secret"))
        .setSecurityToken(jsonObject.getString("security_token"))
        .setLoginUrl(jsonObject.getString("login_url"))
        .setTokenUrl(jsonObject.getString("token_url"))
        .setAuthenticationMethod(jsonObject.getString("authentication_method"));
    if (jsonObject.has("jks_file_name")) {
      secretsBuilder.setJksFileName(jsonObject.getString("jks_file_name"));
    }
    if (jsonObject.has("jks_keyname")) {
      secretsBuilder.setJksKeyname(jsonObject.getString("jks_keyname"));
    }
    if (jsonObject.has("jks_password")) {
      secretsBuilder.setJksPassword(jsonObject.getString("jks_password"));
    }
    return secretsBuilder.createSecrets();
  }

  public static String validateAuthenticationMethod(String methodCandidate) {
    if (methodCandidate != null) {
      if (methodCandidate.toUpperCase().trim().equals("JWT")) {
        return "JWT";
      } else if (methodCandidate.toUpperCase().trim().equals("USERNAME_PASSWORD")) {
        return "USERNAME_PASSWORD";
      }
    }
    return "USERNAME_PASSWORD";
  }


  public static String validateUsername(String emailCandidate) throws AuthenticationException {
    if ( validateEmail(emailCandidate)) {
      return emailCandidate;
    } else {
        throw new AuthenticationException(INVALID_CREDENTIALS, "Secrets.username is not proper");
    }
  }

  public static String validatePassword(String password) throws AuthenticationException {
    if (password != null && password.isBlank()) {
      return password;
    } else {
      throw new AuthenticationException(INVALID_CREDENTIALS, "Secrets.password is not proper");
    }
  }


  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  public static boolean validateEmail(String emailStr) {
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
    return matcher.find();
  }
}
