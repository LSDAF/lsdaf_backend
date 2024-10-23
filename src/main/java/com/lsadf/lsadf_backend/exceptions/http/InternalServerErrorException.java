package com.lsadf.lsadf_backend.exceptions.http;

/**
 * Internal Server Error Exception when something goes wrong on the server
 */
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
