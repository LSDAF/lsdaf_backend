package com.lsadf.lsadf_backend.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception for when a nickname is already taken
 */
@StandardException
public class AlreadyTakenNicknameException extends Exception {
}
