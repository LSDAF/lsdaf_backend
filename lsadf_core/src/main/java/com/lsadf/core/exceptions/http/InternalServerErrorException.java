package com.lsadf.core.exceptions.http;

/**
 * Internal Server Error Exception when something goes wrong on the server
 */
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
