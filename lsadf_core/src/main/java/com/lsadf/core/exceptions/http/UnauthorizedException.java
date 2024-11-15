package com.lsadf.core.exceptions.http;

/**
 * Authentication Exception
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
