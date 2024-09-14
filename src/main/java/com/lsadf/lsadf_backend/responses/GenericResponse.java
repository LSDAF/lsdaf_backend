package com.lsadf.lsadf_backend.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Generic POJO Response for all API controllers
 * @param <T> Type of object to return
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 5392685232533641077L;
    private int status;
    private String message;
    private T data;
}
