package com.lsadf.core.utils;


import com.lsadf.lsadf_backend.responses.GenericResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for response creation
 */
@UtilityClass
public class ResponseUtils {
    /**
     * Builds a response
     *
     * @param status      HTTP status of the response
     * @param message     Human readable message status
     * @param responseObj Object to return
     * @param <T>         Type of object to return
     * @return ResponseEntity of GenericResponse containing all inputs
     */
    public static <T> ResponseEntity<GenericResponse<T>> generateResponse(HttpStatus status, String message, Object responseObj) {
        GenericResponse response = generateGenericResponse(status, message, responseObj);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Builds a response
     *
     * @param status      HTTP status of the response
     * @param responseObj Object to return
     * @param <T>         Type of object to return
     * @return ResponseEntity of GenericResponse containing all inputs
     */
    public static <T> ResponseEntity<GenericResponse<T>> generateResponse(HttpStatus status, Object responseObj) {
        return generateResponse(status, null, responseObj);
    }

    /**
     * Builds a response
     *
     * @param status HTTP status of the response
     * @param <T>    Type of object to return
     * @return ResponseEntity of GenericResponse containing all inputs
     */
    public static <T> ResponseEntity<GenericResponse<T>> generateResponse(HttpStatus status) {
        return generateResponse(status, null, null);
    }

    /**
     * private method to build a GenericResponse
     *
     * @param status      HTTP status of the response
     * @param message     Human-readable message status
     * @param responseObj Object to return
     * @param <T>         Type of object to return
     * @return GenericResponse containing all inputs
     */
    private static <T> GenericResponse<T> generateGenericResponse(HttpStatus status, String message, T responseObj) {
        return GenericResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(responseObj)
                .build();
    }
}
