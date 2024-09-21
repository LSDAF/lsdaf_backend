package com.lsadf.lsadf_backend.exceptions;

public class AlreadyExistingUserException extends Exception {
    public AlreadyExistingUserException(String message) {
        super(message);
    }
}
