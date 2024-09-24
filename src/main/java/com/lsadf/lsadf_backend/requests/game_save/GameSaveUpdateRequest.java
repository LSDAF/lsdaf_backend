package com.lsadf.lsadf_backend.requests.game_save;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSaveUpdateRequest {
    @JsonProperty(value = JsonAttributes.Characteristic.HEALTH_POINTS)
    @PositiveOrZero
    private long healthPoints;

    @JsonProperty(value = JsonAttributes.Characteristic.ATTACK)
    @PositiveOrZero
    private long attack;
}
