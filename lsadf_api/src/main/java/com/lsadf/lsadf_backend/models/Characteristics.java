package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import com.lsadf.lsadf_backend.constants.JsonViews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Characteristics.*;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Characteristics", description = "Characteristics object")
@Data
@Builder
@JsonPropertyOrder({ATTACK, CRIT_CHANCE, CRIT_DAMAGE, HEALTH, RESISTANCE})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public class Characteristics implements Model {
    @Serial
    private static final long serialVersionUID = 5623465292659597625L;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = ATTACK)
    @Schema(description = "Attack level", example = "100")
    private Long attack;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = CRIT_CHANCE)
    @Schema(description = "Crit chance level", example = "100")
    private Long critChance;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = CRIT_DAMAGE)
    @Schema(description = "Crit damage level", example = "100")
    private Long critDamage;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = HEALTH)
    @Schema(description = "Health level", example = "100")
    private Long health;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = RESISTANCE)
    @Schema(description = "Resistance level", example = "100")
    private Long resistance;
}
