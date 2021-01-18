package com.chgustaf.salesforce.authentication.exceptions;

public class AuthenticationException extends Exception {

  public enum Code {
    UNAUTHORIZED,
    UNKNOWN_AUTHENTICATION_FLOW,
    UNRECOVERABLE_KEY_EXCEPTION,
    CERTIFICATE_EXCEPTION,
    NO_SUCH_ALGORITHM_EXCEPTION,
    KEY_STORE_EXCEPTION,
    UNSUPPORTED_ENCODING_EXCEPTION,
    SIGNATURE_EXCEPTION,
    INVALID_KEY_EXCEPTION,
    FILE_NOT_FOUND_EXCEPTION,
    INVALID_CREDENTIALS
  }

  Code code;

  public AuthenticationException(final Code code, final String message) {
    super(message);
    this.code = code;
  }

  public Code getCode() {
    return code;
  }
}
