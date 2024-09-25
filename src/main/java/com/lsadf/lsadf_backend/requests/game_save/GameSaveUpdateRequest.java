package com.lsadf.lsadf_backend.requests.game_save;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSaveUpdateRequest {
    @JsonProperty(value = HP)
    @PositiveOrZero
    private long healthPoints;

    @JsonProperty(value = ATTACK)
    @PositiveOrZero
    private long attack;

    @JsonProperty(value = NICKNAME)
    private String nickname;
}
