package com.lsadf.lsadf_backend.requests.characteristics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.core.constants.JsonAttributes.Characteristics.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ATTACK, CRIT_CHANCE, CRIT_DAMAGE, HEALTH, RESISTANCE})
public class CharacteristicsRequest implements Request {
    @Serial
    private static final long serialVersionUID = 1865696066274976174L;

    @Schema(description = "The attack level of the user", example = "100")
    @JsonProperty(value = ATTACK)
    @Positive
    private Long attack;

    @Schema(description = "The critical chance level of the user", example = "100")
    @JsonProperty(value = CRIT_CHANCE)
    @Positive
    private Long critChance;

    @Schema(description = "The critical damage level of the user", example = "100")
    @JsonProperty(value = CRIT_DAMAGE)
    @Positive
    private Long critDamage;

    @Schema(description = "The health level of the user", example = "100")
    @JsonProperty(value = HEALTH)
    @Positive
    private Long health;

    @Schema(description = "The resistance level of the user", example = "100")
    @JsonProperty(value = RESISTANCE)
    @Positive
    private Long resistance;
}
