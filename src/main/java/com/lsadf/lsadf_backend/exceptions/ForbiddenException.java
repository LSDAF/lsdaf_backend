package com.lsadf.lsadf_backend.exceptions;

public class ForbiddenException extends Exception {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super();
    }
}
