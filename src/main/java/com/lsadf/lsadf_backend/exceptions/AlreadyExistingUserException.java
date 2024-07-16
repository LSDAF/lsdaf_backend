package com.lsadf.lsadf_backend.exceptions;

public class AlreadyExistingUserException extends RuntimeException {
    public AlreadyExistingUserException(String message) {
        super(message);
    }
}
