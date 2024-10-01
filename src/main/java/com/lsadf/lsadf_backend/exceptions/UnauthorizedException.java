package com.lsadf.lsadf_backend.exceptions;

/**
 * Authentication Exception
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
