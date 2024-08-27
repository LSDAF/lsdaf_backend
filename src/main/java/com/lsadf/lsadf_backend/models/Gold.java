package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Gold.AMOUNT;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Gold.GAME_SAVE_ID;;

@Data
@Schema(name = "Gold", description = "Gold amount Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ID, GAME_SAVE_ID, AMOUNT})
public class Gold {
    @JsonProperty(value = GAME_SAVE_ID)
    @Schema(description = "Game Save Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private String gameSaveId;

    @JsonProperty(value = AMOUNT)
    @PositiveOrZero
    @Schema(description = "Gold amount", example = "26093")
    private long amount;
}
