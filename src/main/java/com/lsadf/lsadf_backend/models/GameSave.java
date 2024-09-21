package com.lsadf.lsadf_backend.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Currency.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;

/**
 * Game Save DTO
 */
@Data
@Schema(name = "GameSave", description = "Game Save Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ID, USER_ID, GOLD, DIAMOND, EMERALD, AMETHYST, HP, ATTACK, CREATED_AT, UPDATED_AT})
public class GameSave {
    @JsonProperty(value = JsonAttributes.ID)
    @Schema(description = "Game Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private String id;

    @JsonProperty(value = USER_ID)
    private String userId;

    @JsonProperty(value = USER_EMAIL)
    private String userEmail;

    @JsonProperty(value = GOLD)
    @Schema(description = "Gold amount", example = "260000")
    @Builder.Default
    private long gold = 0L;

    @JsonProperty(value = DIAMOND)
    @Schema(description = "Diamond amount", example = "260")
    @Builder.Default
    private long diamond = 0L;

    @JsonProperty(value = EMERALD)
    @Schema(description = "Emerald amount", example = "260")
    @Builder.Default
    private long emerald = 0L;

    @JsonProperty(value = AMETHYST)
    @Schema(description = "Amethyst amount", example = "260")
    @Builder.Default
    private long amethyst = 0L;

    @JsonProperty(value = HP)
    @Schema(description = "Health points", example = "50")
    @Builder.Default
    private long healthPoints = 10;

    @JsonProperty(value = ATTACK)
    @Schema(description = "Attack points", example = "260")
    @Builder.Default
    private long attack = 1;

    @JsonProperty(value = CREATED_AT)
    @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
    private Date createdAt;

    @JsonProperty(value = UPDATED_AT)
    @Schema(description = "Update date", example = "2022-01-01 00:00:00.000")
    private Date updatedAt;
}
