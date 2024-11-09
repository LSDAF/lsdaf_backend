package com.lsadf.lsadf_backend.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import com.lsadf.lsadf_backend.constants.JsonViews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Date;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.CREATED_AT;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.UPDATED_AT;

/**
 * Game Save DTO
 */
@Data
@Schema(name = "GameSave", description = "Game Save Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSave implements Model {

    @Serial
    private static final long serialVersionUID = -2686370647354845265L;

    // Admin fields

    // Internal fields

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = JsonAttributes.ID)
    @Schema(description = "Game Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private String id;

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = USER_EMAIL)
    private String userEmail;

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = CREATED_AT)
    @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
    private Date createdAt;

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = UPDATED_AT)
    @Schema(description = "Update date", example = "2022-01-01 00:00:00.000")
    private Date updatedAt;

    // External fields

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = NICKNAME)
    private String nickname;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = CHARACTERISTICS)
    private Characteristics characteristics;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = CURRENCY)
    private Currency currency;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = STAGE)
    private Stage stage;

}
