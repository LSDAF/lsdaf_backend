package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Stage.CURRENT_STAGE;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Stage.MAX_STAGE;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(name = "Stage", description = "Stage object containing the player's game progress")
@JsonPropertyOrder({CURRENT_STAGE, MAX_STAGE})
public class Stage implements Model {

    @Serial
    private static final long serialVersionUID = -7126306428235414817L;

    @Schema(description = "The Current game stage", example = "26")
    @JsonProperty(value = CURRENT_STAGE)
    private Long currentStage;

    @Schema(description = "The Maximum game stage", example = "26")
    @JsonProperty(value = MAX_STAGE)
    private Long maxStage;
}
