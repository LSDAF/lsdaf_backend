package com.lsadf.lsadf_backend.requests.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Filter.TYPE;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Filter.VALUE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    @Schema(description = "Type of filter", example = "name")
    @JsonProperty(value = TYPE)
    private String type;

    @Schema(description = "Value of filter", example = "toto")
    @JsonProperty(value = VALUE)
    private String value;
}
