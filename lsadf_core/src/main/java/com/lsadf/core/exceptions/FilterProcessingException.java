package com.lsadf.core.exceptions;

/** Exception for when there is an error processing a filter */
public class FilterProcessingException extends RuntimeException {
  public FilterProcessingException(String message) {
    super(message);
  }

  public FilterProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
