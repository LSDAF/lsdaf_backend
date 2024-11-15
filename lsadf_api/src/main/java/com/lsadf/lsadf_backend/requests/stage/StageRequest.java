package com.lsadf.lsadf_backend.requests.stage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.annotations.StageConsistency;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.core.constants.JsonAttributes.Stage.CURRENT_STAGE;
import static com.lsadf.core.constants.JsonAttributes.Stage.MAX_STAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@StageConsistency(currentStageField = "currentStage", maxStageField = "maxStage")
@JsonPropertyOrder({CURRENT_STAGE, MAX_STAGE})
public class StageRequest implements Request {

    @Serial
    private static final long serialVersionUID = -2154269413949156805L;

    @Schema(description = "The Current game stage", example = "26")
    @JsonProperty(value = CURRENT_STAGE)
    @Positive
    private Long currentStage;

    @Schema(description = "The Maximum game stage", example = "260")
    @JsonProperty(value = MAX_STAGE)
    @Positive
    private Long maxStage;
}
