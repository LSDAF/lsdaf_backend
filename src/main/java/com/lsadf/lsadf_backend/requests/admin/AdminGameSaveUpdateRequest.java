package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Currency.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminGameSaveUpdateRequest {
    @JsonProperty(value = NICKNAME)
    @Schema(description = "Nickname of the user", example = "test")
    private String nickname;

    @JsonProperty(value = GOLD)
    @PositiveOrZero
    @Schema(description = "Amount of gold", example = "100")
    private Long gold;

    @JsonProperty(value = DIAMOND)
    @PositiveOrZero
    @Schema(description = "Amount of diamond", example = "100")
    private Long diamond;

    @JsonProperty(value = EMERALD)
    @PositiveOrZero
    @Schema(description = "Amount of emerald", example = "100")
    private Long emerald;

    @JsonProperty(value = AMETHYST)
    @PositiveOrZero
    @Schema(description = "Amount of amethyst", example = "100")
    private Long amethyst;

    @JsonProperty(value = HP)
    @PositiveOrZero
    @Schema(description = "Health points", example = "100")
    private Long healthPoints;

    @JsonProperty(value = ATTACK)
    @PositiveOrZero
    @Schema(description = "Attack points", example = "100")
    private Long attack;
}
