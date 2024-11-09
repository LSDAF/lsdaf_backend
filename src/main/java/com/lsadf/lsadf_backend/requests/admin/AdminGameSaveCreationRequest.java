package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.annotations.Nickname;
import com.lsadf.lsadf_backend.annotations.Uuid;
import com.lsadf.lsadf_backend.requests.Request;
import com.lsadf.lsadf_backend.requests.characteristics.CharacteristicsRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.ID;

/**
 * Request for creating a new game save
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGameSaveCreationRequest implements Request {

    @Serial
    private static final long serialVersionUID = 2925109471468959138L;

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

    @Valid
    @NotNull
    private CharacteristicsRequest characteristics;

    @Valid
    @NotNull
    private CurrencyRequest currency;

    @Valid
    @NotNull
    private StageRequest stage;
}
