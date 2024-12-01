package com.lsadf.core.exceptions;

/** Exception for when a game save already exists */
public class AlreadyExistingGameSaveException extends RuntimeException {
  public AlreadyExistingGameSaveException(String message) {
    super(message);
  }
}
