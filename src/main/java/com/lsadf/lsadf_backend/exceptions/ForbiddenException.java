package com.lsadf.lsadf_backend.exceptions;

/**
 * Exception for when a user is not allowed to access a resource.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super();
    }
}
