package com.lsadf.lsadf_backend.controllers.exception_handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldError {
    private String field;
    private String error;
    private String rejectedValue;
}
