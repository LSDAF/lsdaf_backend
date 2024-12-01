package com.lsadf.core.requests.game_save;

import static com.lsadf.core.constants.JsonAttributes.GameSave.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.annotations.Nickname;
import com.lsadf.core.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSaveUpdateNicknameRequest implements Request {

  @Serial private static final long serialVersionUID = -6478222007381338108L;

  @JsonProperty(value = NICKNAME)
  @Nickname
  @Schema(description = "Nickname of the user", example = "test")
  private String nickname;
}
