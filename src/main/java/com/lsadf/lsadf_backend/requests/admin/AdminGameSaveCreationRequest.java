package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.annotations.StageConsistency;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Currency.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.ID;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Stage.CURRENT_STAGE;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Stage.MAX_STAGE;

/**
 * Request for creating a new game save
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@StageConsistency(currentStageField = "currentStage", maxStageField = "maxStage")
public class AdminGameSaveCreationRequest implements Request {

    @Serial
    private static final long serialVersionUID = 2925109471468959138L;

    @Nullable
    @JsonProperty(value = ID)
    @Schema(description = "Id of the game save", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private String id;

    @Email
    @NotBlank
    @JsonProperty(value = USER_EMAIL)
    @Schema(description = "Email of the user", example = "test@test.com")
    private String userEmail;

    @PositiveOrZero
    @JsonProperty(value = GOLD)
    @Schema(description = "Amount of gold", example = "100")
    private Long gold;

    @PositiveOrZero
    @JsonProperty(value = DIAMOND)
    @Schema(description = "Amount of diamond", example = "100")
    private Long diamond;

    @PositiveOrZero
    @JsonProperty(value = EMERALD)
    @Schema(description = "Amount of emerald", example = "100")
    private Long emerald;

    @PositiveOrZero
    @JsonProperty(value = AMETHYST)
    @Schema(description = "Amount of amethyst", example = "100")
    private Long amethyst;

    @Positive
    @JsonProperty(value = HP)
    @Schema(description = "Health points", example = "100")
    private Long healthPoints;

    @Positive
    @JsonProperty(value = ATTACK)
    @Schema(description = "Attack points", example = "100")
    private Long attack;

    @Positive
    @JsonProperty(value = CURRENT_STAGE)
    @Schema(description = "Current game stage", example = "26")
    private Long currentStage;

    @Positive
    @JsonProperty(value = MAX_STAGE)
    @Schema(description = "Max game satge recorded. Should not be smaller than current stage", example = "26")
    private Long maxStage;
}
