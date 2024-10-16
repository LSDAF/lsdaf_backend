package com.lsadf.lsadf_backend.exceptions.http;

import lombok.experimental.StandardException;

/**
 * Exception for when a user is not allowed to access a resource.
 */
@StandardException
public class ForbiddenException extends RuntimeException {
}
