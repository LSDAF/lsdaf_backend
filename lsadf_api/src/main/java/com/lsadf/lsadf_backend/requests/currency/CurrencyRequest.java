package com.lsadf.lsadf_backend.requests.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.core.constants.JsonAttributes.Currency.*;
import static com.lsadf.core.constants.JsonAttributes.Currency.AMETHYST;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({GOLD, DIAMOND, EMERALD, AMETHYST})
public class CurrencyRequest implements Request {

    @Serial
    private static final long serialVersionUID = 1865696066274976174L;

    @Schema(description = "The amount of gold", example = "100")
    @JsonProperty(value = GOLD)
    @PositiveOrZero
    private Long gold;

    @Schema(description = "The amount of diamond", example = "100")
    @JsonProperty(value = DIAMOND)
    @PositiveOrZero
    private Long diamond;

    @Schema(description = "The amount of emerald", example = "100")
    @JsonProperty(value = EMERALD)
    @PositiveOrZero
    private Long emerald;

    @Schema(description = "The amount of amethyst", example = "100")
    @JsonProperty(value = AMETHYST)
    @PositiveOrZero
    private Long amethyst;
}
