package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminGameSaveUpdateRequest {
    @JsonProperty(value = GOLD)
    @PositiveOrZero
    @Nullable
    private Long gold;

    @JsonProperty(value = DIAMOND)
    @PositiveOrZero
    @Nullable
    private Long diamond;

    @JsonProperty(value = EMERALD)
    @PositiveOrZero
    @Nullable
    private Long emerald;

    @JsonProperty(value = AMETHYST)
    @PositiveOrZero
    @Nullable
    private Long amethyst;

    @JsonProperty(value = HP)
    @PositiveOrZero
    @Nullable
    private Long healthPoints;

    @JsonProperty(value = ATTACK)
    @PositiveOrZero
    @Nullable
    private Long attack;
}
