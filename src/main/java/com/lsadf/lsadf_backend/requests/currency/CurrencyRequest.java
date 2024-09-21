package com.lsadf.lsadf_backend.requests.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Currency.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Currency.AMETHYST;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({GOLD, DIAMOND, EMERALD, AMETHYST})
public class CurrencyRequest {
    @Schema(description = "The amount of gold", example = "100")
    @JsonProperty(value = GOLD)
    private Long gold;

    @Schema(description = "The amount of diamond", example = "100")
    @JsonProperty(value = DIAMOND)
    private Long diamond;

    @Schema(description = "The amount of emerald", example = "100")
    @JsonProperty(value = EMERALD)
    private Long emerald;

    @Schema(description = "The amount of amethyst", example = "100")
    @JsonProperty(value = AMETHYST)
    private Long amethyst;
}
