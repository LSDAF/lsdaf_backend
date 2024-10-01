package com.lsadf.lsadf_backend.exceptions;

/**
 * Exception for when a token is invalid or expired.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
