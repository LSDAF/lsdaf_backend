package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Stages DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Stages", description = "Stages object")
@Data
@Builder
@JsonPropertyOrder({
        JsonAttributes.Stage.MAX_STAGE,
        JsonAttributes.Stage.CURRENT_STAGE,
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Stage {
    @JsonProperty(value = JsonAttributes.Stage.MAX_STAGE)
    @Schema(description = "The max stage", example = "10")
    private int max_stage;

    @JsonProperty(value = JsonAttributes.Stage.CURRENT_STAGE)
    @Schema(description = "The current stage", example = "10")
    private int current_stage;
}
