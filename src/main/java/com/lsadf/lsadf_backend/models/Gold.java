package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Gold.AMOUNT;

@Data
@Schema(name = "Gold", description = "Gold amount Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({AMOUNT})
public class Gold {
    @JsonProperty(value = AMOUNT)
    @PositiveOrZero
    @Schema(description = "Gold amount", example = "26093")
    private long amount;
}
