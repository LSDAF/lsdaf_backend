package com.lsadf.core.exceptions.http;

/** Exception for when a resource is not found. */
public class NotFoundException extends RuntimeException {
  public NotFoundException() {
    super();
  }

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
