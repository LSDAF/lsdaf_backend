package com.lsadf.lsadf_backend.exceptions;

/**
 * Exception for when a resource is not found.
 */
public class NotFoundException extends Exception {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
