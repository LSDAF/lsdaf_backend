package com.lsadf.lsadf_backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;

/**
 * Game Save DTO
 */
@Data
@RequiredArgsConstructor
@Schema(name = "GameSave", description = "Game Save Object")
@Builder
@AllArgsConstructor
public class GameSave {
    @JsonProperty(value = JsonAttributes.ID)
    @Schema(description = "Game Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private final String id;

    @JsonIgnore
    private final User user;

    @JsonProperty(value = GOLD)
    @Schema(description = "Gold amount", example = "260000")
    @Builder.Default
    private long gold = 0L;

    @JsonProperty(value = HP)
    @Schema(description = "Health points", example = "50")
    @Builder.Default
    private long healthPoints = 10;

    @JsonProperty(value = ATTACK)
    @Schema(description = "Attack points", example = "260")
    @Builder.Default
    private long attack = 1;

    @JsonProperty(value = CREATED_AT)
    @Schema(description = "Creation date", example = "2022-01-01T00:00:00.000Z")
    private final Date createdAt;

    @JsonProperty(value = UPDATED_AT)
    @Schema(description = "Update date", example = "2022-01-01T00:00:00.000Z")
    private final Date updatedAt;
}
