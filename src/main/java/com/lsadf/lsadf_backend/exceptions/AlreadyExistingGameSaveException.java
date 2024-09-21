package com.lsadf.lsadf_backend.exceptions;

public class AlreadyExistingGameSaveException extends Exception {
    public AlreadyExistingGameSaveException(String message) {
        super(message);
    }
}
