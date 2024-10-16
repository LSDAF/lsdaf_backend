package com.lsadf.lsadf_backend.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception for when a user already exists
 */
@StandardException
public class AlreadyExistingUserException extends RuntimeException {
}
