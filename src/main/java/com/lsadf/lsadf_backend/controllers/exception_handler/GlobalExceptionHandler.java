package com.lsadf.lsadf_backend.controllers.exception_handler;

import com.lsadf.lsadf_backend.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.AlreadyTakenNicknameException;
import com.lsadf.lsadf_backend.exceptions.http.*;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Exception handler for MethodArgumentNotValidException
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity containing the errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<List<FieldError>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);
        List<FieldError> errors = e.getFieldErrors()
                .stream()
                .map(error -> {
                    String field = error.getField();
                    String msg = error.getDefaultMessage();
                    String rejectedValue = error.getRejectedValue() == null ? null : error.getRejectedValue().toString();
                    return new FieldError(field, msg, rejectedValue);
                })
                .toList();
        String message = "Validation failed for the following fields";
        return generateResponse(HttpStatus.BAD_REQUEST, message, errors);
    }

    /**
     * Exception handler for AlreadyExistingGameSaveException
     * @param e AlreadyExistingGameSaveException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<GenericResponse<Void>> handleValidationException(HandlerMethodValidationException e) {
        log.error("HandlerMethodValidationException: ", e);
        List<FieldError> fieldErrors = new ArrayList<>();
        e.getAllValidationResults().forEach(parameterValidationResult -> {
            String rejectedValue = Objects.requireNonNull(parameterValidationResult.getArgument()).toString();
            parameterValidationResult.getResolvableErrors().forEach(resolvableError -> {
                String msg = resolvableError.getDefaultMessage();
                fieldErrors.add(new FieldError(null, msg, rejectedValue));
            });
        });
        return generateResponse(HttpStatus.BAD_REQUEST, "Fields validation failed", fieldErrors);
    }

    /**
     * Exception handler for HttpMessageNotReadableException
     * @param e HttpMessageNotReadableException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: ", e);
        String msg = e.getMessage();
        return generateResponse(HttpStatus.BAD_REQUEST, "Message not readable: " + msg, null);
    }

    /**
     * Exception handler for UnauthorizedException
     * @param e UnauthorizedException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericResponse<Void>> handleUnauthorizedException(UnauthorizedException e) {
        log.error("UnauthorizedException: ", e);
        return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized: " + e.getMessage(), null);
    }

    /**
     * Exception handler for NotFoundException
     * @param e NotFoundException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericResponse<Void>> handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException: ", e);
        return generateResponse(HttpStatus.NOT_FOUND, "Not found: " + e.getMessage(), null);
    }

    /**
     * Exception handler for ForbiddenException
     * @param e ForbiddenException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<GenericResponse<Void>> handleForbiddenException(ForbiddenException e) {
        log.error("ForbiddenException: ", e);
        return generateResponse(HttpStatus.FORBIDDEN, "Forbidden: " + e.getMessage(), null);
    }

    /**
     * Exception handler for InternalServerErrorException
     * @param e InternalServerErrorException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<GenericResponse<Void>> handleInternalServerErrorException(InternalServerErrorException e) {
        log.error("InternalServerErrorException: ", e);
        return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage(), null);
    }

    /**
     * Exception handler for BadRequestException
     * @param e BadRequestException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GenericResponse<Void>> handleBadRequestException(BadRequestException e) {
        log.error("BadRequestException: ", e);
        return generateResponse(HttpStatus.BAD_REQUEST, "Bad request: " + e.getMessage(), null);
    }

    /**
     * Exception handler for AlreadyExistingGameSaveException
     * @param e AlreadyExistingGameSaveException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(AlreadyExistingGameSaveException.class)
    public ResponseEntity<GenericResponse<Void>> handleAlreadyExistingGameSaveException(AlreadyExistingGameSaveException e) {
        log.error("AlreadyExistingGameSaveException: ", e);
        return generateResponse(HttpStatus.BAD_REQUEST, "Game save already exists: " + e.getMessage(), null);
    }

    /**
     * Exception handler for AlreadyExistingUserException
     * @param e AlreadyExistingUserException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(AlreadyExistingUserException.class)
    public ResponseEntity<GenericResponse<Void>> handleAlreadyExistingUserException(AlreadyExistingUserException e) {
        log.error("AlreadyExistingUserException: ", e);
        return generateResponse(HttpStatus.BAD_REQUEST, "User already exists: " + e.getMessage(), null);
    }

    /**
     * Exception handler for AlreadyTakenNicknameException
     * @param e AlreadyTakenNicknameException
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(AlreadyTakenNicknameException.class)
    public ResponseEntity<GenericResponse<Void>> handleAlreadyTakenNicknameException(AlreadyTakenNicknameException e) {
        log.error("AlreadyTakenNicknameException: ", e);
        return generateResponse(HttpStatus.BAD_REQUEST, "Nickname already taken: " + e.getMessage(), null);
    }

    /**
     * Generic Exception handler for Exception
     * @param e Exception
     * @return ResponseEntity containing the error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Void>> handleException(Exception e) {
        log.error("Exception: " + e);
        return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage(), null);
    }
}
