package com.lsadf.lsadf_backend.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception for when a game save already exists
 */
@StandardException
public class AlreadyExistingGameSaveException extends RuntimeException {
}
