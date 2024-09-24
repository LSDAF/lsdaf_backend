package com.lsadf.lsadf_backend.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;


/**
 * Game Save DTO
 */
@Data
@Schema(name = "GameSave", description = "Game Save Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        JsonAttributes.ID,
        JsonAttributes.GameSave.NICKNAME,
        JsonAttributes.GameSave.CURRENCIES,
        JsonAttributes.GameSave.CHARACTERISTICS,
        JsonAttributes.GameSave.STAGES,
        JsonAttributes.CREATED_AT,
        JsonAttributes.UPDATED_AT,
})
public class GameSave {
    @JsonProperty(value = JsonAttributes.ID)
    @Schema(description = "Game Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private String id;

    @JsonProperty(value = JsonAttributes.GameSave.NICKNAME)
    @Schema(description = "Game nickname", example = "xXxGamerxXx")
    private String nickname;

    @JsonProperty(value = JsonAttributes.GameSave.CURRENCIES)
    @Schema(description = "Game currencies")
    private Currencies currencies;

    @JsonProperty(value = JsonAttributes.GameSave.CHARACTERISTICS)
    @Schema(description = "Game characteristics")
    private Characteristics characteristics;

    @JsonProperty(value = JsonAttributes.GameSave.STAGES)
    @Schema(description = "Game stages")
    private Stages stages;

    @JsonProperty(value = JsonAttributes.CREATED_AT)
    @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
    private Date createdAt;

    @JsonProperty(value = JsonAttributes.UPDATED_AT)
    @Schema(description = "Update date", example = "2022-01-01 00:00:00.000")
    private Date updatedAt;
}
