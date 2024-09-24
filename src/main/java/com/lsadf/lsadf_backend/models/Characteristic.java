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
        JsonAttributes.Characteristic.ATTACK,
        JsonAttributes.Characteristic.CRITICAL_CHANCE,
        JsonAttributes.Characteristic.CRITICAL_DAMAGE,
        JsonAttributes.Characteristic.HEALTH_POINTS,
        JsonAttributes.Characteristic.RESISTANCE,
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Characteristic {
    @JsonProperty(value = JsonAttributes.Characteristic.ATTACK)
    @Schema(description = "The attack level of the character", example = "10")
    private int attack_level;

    @JsonProperty(value = JsonAttributes.Characteristic.CRITICAL_CHANCE)
    @Schema(description = "The critical chance level of the character", example = "10")
    private int critical_chance_level;

    @JsonProperty(value = JsonAttributes.Characteristic.CRITICAL_DAMAGE)
    @Schema(description = "The critical damage level of the character", example = "10")
    private int critical_damage_level;

    @JsonProperty(value = JsonAttributes.Characteristic.HEALTH_POINTS)
    @Schema(description = "The health points level of the character", example = "10")
    private int health_points_level;

    @JsonProperty(value = JsonAttributes.Characteristic.RESISTANCE)
    @Schema(description = "The resistance level of the character", example = "10")
    private int resistance_level;
}
