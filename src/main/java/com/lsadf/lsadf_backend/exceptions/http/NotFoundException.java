package com.lsadf.lsadf_backend.exceptions.http;

import lombok.experimental.StandardException;

/**
 * Exception for when a resource is not found.
 */
@StandardException
public class NotFoundException extends RuntimeException {
}
