package com.lsadf.lsadf_backend.exceptions;

public class AlreadyExistingGameSaveException extends RuntimeException {
    public AlreadyExistingGameSaveException(String message) {
        super(message);
    }
}
