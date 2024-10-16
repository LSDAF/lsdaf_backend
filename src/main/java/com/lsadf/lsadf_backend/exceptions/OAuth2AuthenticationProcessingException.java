package com.lsadf.lsadf_backend.exceptions;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

/**
 * Exception for when an OAuth2 authentication fails.
 */
@StandardException
public class OAuth2AuthenticationProcessingException extends AuthenticationException {
}
