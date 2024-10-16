package com.lsadf.lsadf_backend.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception for when a token is invalid or expired.
 */
@StandardException
public class InvalidTokenException extends RuntimeException {
}
