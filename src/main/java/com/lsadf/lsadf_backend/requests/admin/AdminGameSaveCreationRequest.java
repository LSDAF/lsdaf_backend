package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
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

/**
 * Request for creating a new game save
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGameSaveCreationRequest {
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
    private long gold;

    @PositiveOrZero
    @JsonProperty(value = DIAMOND)
    @Schema(description = "Amount of diamond", example = "100")
    private long diamond;

    @PositiveOrZero
    @JsonProperty(value = EMERALD)
    @Schema(description = "Amount of emerald", example = "100")
    private long emerald;

    @PositiveOrZero
    @JsonProperty(value = AMETHYST)
    @Schema(description = "Amount of amethyst", example = "100")
    private long amethyst;

    @Positive
    @JsonProperty(value = HP)
    @Schema(description = "Health points", example = "100")
    private long healthPoints;

    @Positive
    @JsonProperty(value = ATTACK)
    @Schema(description = "Attack points", example = "100")
    private long attack;
}
