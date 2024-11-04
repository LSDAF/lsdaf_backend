package com.lsadf.lsadf_backend.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.lsadf_backend.constants.JsonViews;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.GenericResponse.*;

/**
 * Generic POJO Response for all API controllers
 * @param <T> Type of object to return
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({STATUS, MESSAGE, DATA})
public class GenericResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 5392685232533641077L;

    @JsonProperty(value = STATUS)
    @JsonView(JsonViews.External.class)
    private int status;

    @JsonProperty(value = MESSAGE)
    @JsonView(JsonViews.External.class)
    private String message;

    @JsonProperty(value = DATA)
    @JsonView(JsonViews.External.class)
    private transient T data;
}
