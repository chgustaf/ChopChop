package com.salesforce.authentication.jwt;

import static com.salesforce.authentication.exceptions.AuthenticationException.Code.CERTIFICATE_EXCEPTION;
import static com.salesforce.authentication.exceptions.AuthenticationException.Code.INVALID_KEY_EXCEPTION;
import static com.salesforce.authentication.exceptions.AuthenticationException.Code.KEY_STORE_EXCEPTION;
import static com.salesforce.authentication.exceptions.AuthenticationException.Code.NO_SUCH_ALGORITHM_EXCEPTION;
import static com.salesforce.authentication.exceptions.AuthenticationException.Code.SIGNATURE_EXCEPTION;
import static com.salesforce.authentication.exceptions.AuthenticationException.Code.UNRECOVERABLE_KEY_EXCEPTION;
import static com.salesforce.authentication.exceptions.AuthenticationException.Code.UNSUPPORTED_ENCODING_EXCEPTION;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.salesforce.authentication.secrets.Secrets;
import com.salesforce.authentication.exceptions.AuthenticationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.json.JSONObject;

public class JWTFactory {

  static Secrets secrets;
  static PrivateKey privateKey;

  public JWTFactory(Secrets secrets)
      throws IOException, AuthenticationException {
    this.secrets = secrets;
    readKey();
  }

  public String createJWT() throws AuthenticationException {
    String header = createHeader();
    String claim = createClaimSet();
    String encodedHeader = encode64Safe(header.getBytes(UTF_8));
    String encodedClaim = encode64Safe(claim.getBytes(UTF_8));;
    String signature = sign(encodedHeader + "." + encodedClaim);
    String jwt = encodedHeader + "." + encodedClaim + "." + signature;
    return jwt;
  }

  private String createClaimSet() {
    return new JSONObject()
        .put("iss", secrets.getConsumerKey())
        .put("aud", secrets.getLoginUrl())
        .put("sub", secrets.getUsername())
        .put("exp", Instant.now().plus(2, ChronoUnit.MINUTES).getEpochSecond())
        .toString();
  }

  private String createHeader() {
      return new JSONObject()
          .put("alg", "RS256")
          .put("typ", "JWT")
          .toString();
  }

  private static String sign(String theString) throws AuthenticationException {
    Signature signature = null;
    String signedPayload = null;
    try {
      signature = Signature.getInstance("SHA256withRSA");
      signature.initSign((PrivateKey) secrets.getPrivateKey());
      signature.update(theString.getBytes("UTF-8"));
      signedPayload = encode64Safe(signature.sign());

    } catch (NoSuchAlgorithmException e) {
      throw new AuthenticationException(NO_SUCH_ALGORITHM_EXCEPTION, e.getMessage());
    } catch (UnsupportedEncodingException e) {
      throw new AuthenticationException(UNSUPPORTED_ENCODING_EXCEPTION, e.getMessage());
    } catch (SignatureException e) {
      throw new AuthenticationException(SIGNATURE_EXCEPTION, e.getMessage());
    } catch (InvalidKeyException e) {
      throw new AuthenticationException(INVALID_KEY_EXCEPTION, e.getMessage());
    }

    return signedPayload;
  }

  public static String encode64Safe(byte[] byteArr) {
    return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(byteArr);
  }

  public static void readKey() throws AuthenticationException, IOException {
    String keyStorePassword = "test2020"; // The password that the keystore
    // is locked with
    KeyStore keystore = null;
    try {
      keystore = KeyStore.getInstance("JKS");
      FileInputStream fis =
          new FileInputStream(ClassLoader.getSystemResource("here.jks").getPath());

      keystore.load(fis, keyStorePassword.toCharArray());
      privateKey = (PrivateKey)
          keystore.getKey("mykey",
              keyStorePassword.toCharArray());
      secrets.setPrivateKey(privateKey);

    } catch (KeyStoreException e) {
      throw new AuthenticationException(KEY_STORE_EXCEPTION, e.getMessage());
    } catch (CertificateException e) {
      throw new AuthenticationException(CERTIFICATE_EXCEPTION, e.getMessage());
    } catch (UnrecoverableKeyException e) {
      throw new AuthenticationException(UNRECOVERABLE_KEY_EXCEPTION, e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      throw new AuthenticationException(NO_SUCH_ALGORITHM_EXCEPTION, e.getMessage());
    }
  }
}
