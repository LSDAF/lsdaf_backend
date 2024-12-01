package com.lsadf.core.exceptions;

/** Exception for when a user already exists */
public class AlreadyExistingUserException extends RuntimeException {
  public AlreadyExistingUserException(String message) {
    super(message);
  }
}
