package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Characteristics DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Characteristics", description = "Characteristics object")
@Data
@Builder
@JsonPropertyOrder({
        JsonAttributes.Characteristics.ATTACK,
        JsonAttributes.Characteristics.CRITICAL_CHANCE,
        JsonAttributes.Characteristics.CRITICAL_DAMAGE,
        JsonAttributes.Characteristics.HEALTH_POINTS,
        JsonAttributes.Characteristics.RESISTANCE,
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Characteristics {
    @JsonProperty(value = JsonAttributes.Characteristics.ATTACK)
    @Schema(description = "The attack level of the character", example = "10")
    private int attack_level;

    @JsonProperty(value = JsonAttributes.Characteristics.CRITICAL_CHANCE)
    @Schema(description = "The critical chance level of the character", example = "10")
    private int critical_chance_level;

    @JsonProperty(value = JsonAttributes.Characteristics.CRITICAL_DAMAGE)
    @Schema(description = "The critical damage level of the character", example = "10")
    private int critical_damage_level;

    @JsonProperty(value = JsonAttributes.Characteristics.HEALTH_POINTS)
    @Schema(description = "The health points level of the character", example = "10")
    private int health_points_level;

    @JsonProperty(value = JsonAttributes.Characteristics.RESISTANCE)
    @Schema(description = "The resistance level of the character", example = "10")
    private int resistance_level;
}
