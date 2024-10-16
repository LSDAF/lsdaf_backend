package com.lsadf.lsadf_backend.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception for when a password is wrong.
 */
@StandardException
public class WrongPasswordException extends RuntimeException {
}
