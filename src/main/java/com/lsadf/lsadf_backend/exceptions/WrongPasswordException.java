package com.lsadf.lsadf_backend.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException() {
        super();
    }
}
