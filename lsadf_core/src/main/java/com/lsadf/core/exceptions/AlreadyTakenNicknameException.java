package com.lsadf.core.exceptions;

/**
 * Exception for when a nickname is already taken
 */
public class AlreadyTakenNicknameException extends RuntimeException {
    public AlreadyTakenNicknameException(String message) {
        super(message);
    }
}
