package com.lsadf.core.requests.admin;

import static com.lsadf.core.constants.JsonAttributes.GameSave.*;
import static com.lsadf.core.constants.JsonAttributes.ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.annotations.Nickname;
import com.lsadf.core.annotations.Uuid;
import com.lsadf.core.requests.Request;
import com.lsadf.core.requests.characteristics.CharacteristicsRequest;
import com.lsadf.core.requests.currency.CurrencyRequest;
import com.lsadf.core.requests.stage.StageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Request for creating a new game save */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGameSaveCreationRequest implements Request {

  @Serial private static final long serialVersionUID = 2925109471468959138L;

  @Uuid(nullable = true)
  @JsonProperty(value = ID)
  @Schema(description = "Id of the game save", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
  private String id;

  @Email
  @NotNull
  @JsonProperty(value = USER_EMAIL)
  @Schema(description = "Email of the user", example = "test@test.com")
  private String userEmail;

  @Nickname
  @Schema(description = "Nickname of the user in the game save", example = "Toto")
  private String nickname;

  @Valid @NotNull private CharacteristicsRequest characteristics;

  @Valid @NotNull private CurrencyRequest currency;

  @Valid @NotNull private StageRequest stage;
}
