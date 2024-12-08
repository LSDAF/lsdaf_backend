package com.lsadf.core.exceptions;

/** Exception for when an item client id already exists */
public class AlreadyExistingItemClientIdException extends RuntimeException {
  public AlreadyExistingItemClientIdException(String message) {
    super(message);
  }
}
