package com.lsadf.lsadf_backend.exceptions.http;

/**
 * Bad Request Exception
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
