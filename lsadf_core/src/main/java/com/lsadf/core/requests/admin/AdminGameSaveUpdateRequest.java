package com.lsadf.core.requests.admin;

import static com.lsadf.core.constants.JsonAttributes.GameSave.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.annotations.Nickname;
import com.lsadf.core.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminGameSaveUpdateRequest implements Request {

  @Serial private static final long serialVersionUID = -1619677650296221394L;

  @JsonProperty(value = NICKNAME)
  @Nickname
  @Schema(description = "Nickname of the user", example = "test")
  private String nickname;
}
