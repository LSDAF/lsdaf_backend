package com.lsadf.lsadf_backend.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.util.Date;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;

/**
 * Game Save DTO
 */
@Data
@Schema(name = "GameSave", description = "Game Save Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ID, USER_ID, USER_EMAIL, CURRENCY, STAGE, NICKNAME, HP, ATTACK, CREATED_AT, UPDATED_AT})
public class GameSave implements Model {

    @Serial
    private static final long serialVersionUID = -2686370647354845265L;

    @JsonProperty(value = JsonAttributes.ID)
    @Schema(description = "Game Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private String id;

    @JsonProperty(value = USER_ID)
    private String userId;

    @JsonProperty(value = USER_EMAIL)
    private String userEmail;

    @JsonProperty(value = NICKNAME)
    private String nickname;

    @JsonProperty(value = CURRENCY)
    private Currency currency;

    @JsonProperty(value = STAGE)
    private Stage stage;

    @JsonProperty(value = HP)
    @Schema(description = "Health points", example = "50")
    @Builder.Default
    private Long healthPoints = 10L;

    @JsonProperty(value = ATTACK)
    @Schema(description = "Attack points", example = "260")
    @Builder.Default
    private Long attack = 1L;

    @JsonProperty(value = CREATED_AT)
    @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
    private Date createdAt;

    @JsonProperty(value = UPDATED_AT)
    @Schema(description = "Update date", example = "2022-01-01 00:00:00.000")
    private Date updatedAt;
}
