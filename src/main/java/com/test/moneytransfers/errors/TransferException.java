package com.test.moneytransfers.errors;

public class TransferException extends RuntimeException {

  public TransferException(String message) {
    super(message);
  }
}
