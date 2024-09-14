package com.lsadf.lsadf_backend.requests.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    @Schema(description = "Type of filter", example = "name")
    private String type;

    @Schema(description = "Value of filter", example = "toto")
    private String value;
}
