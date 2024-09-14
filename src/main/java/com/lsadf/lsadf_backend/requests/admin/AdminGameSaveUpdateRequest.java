package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private long gold;

    @JsonProperty(value = HP)
    @PositiveOrZero
    private long healthPoints;

    @JsonProperty(value = ATTACK)
    @PositiveOrZero
    private long attack;
}
