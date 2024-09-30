package com.lsadf.lsadf_backend.requests.game_save;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.annotations.Nickname;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSaveUpdateNicknameRequest {
    @JsonProperty(value = NICKNAME)
    @Nickname
    @Schema(description = "Nickname of the user", example = "test")
    private String nickname;
}
