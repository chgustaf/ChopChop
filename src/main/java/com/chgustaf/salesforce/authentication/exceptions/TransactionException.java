package com.chgustaf.salesforce.authentication.exceptions;

public class TransactionException extends Exception {

  private final String code;

  public TransactionException(final String code) {
    super();
    this.code = code;
  }

  public TransactionException(final String message, final Throwable cause, final String code) {
    super(message, cause);
    this.code = code;
  }

  public TransactionException(String message, String code) {
    super(message);
    this.code = code;
  }

  public TransactionException(Throwable cause, String code) {
    super(cause);
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }

}
