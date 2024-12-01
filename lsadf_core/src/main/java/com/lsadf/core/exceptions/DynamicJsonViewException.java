package com.lsadf.core.exceptions;

public class DynamicJsonViewException extends RuntimeException {
  public DynamicJsonViewException(String message) {
    super(message);
  }

  public DynamicJsonViewException(String message, Throwable cause) {
    super(message, cause);
  }
}
