package com.lsadf.lsadf_backend.exceptions.http;

/**
 * Exception for when a resource is not found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
