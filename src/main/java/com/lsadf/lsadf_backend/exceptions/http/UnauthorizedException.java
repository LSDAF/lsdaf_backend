package com.lsadf.lsadf_backend.exceptions.http;

/**
 * Authentication Exception
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
