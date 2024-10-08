package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Currency.*;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Currency", description = "Currency object")
@Data
@Builder
@JsonPropertyOrder({GOLD, DIAMOND, EMERALD, AMETHYST})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Currency implements Model {

    @Serial
    private static final long serialVersionUID = 33494087785391763L;

    @Schema(description = "The amount of gold", example = "100")
    private Long gold;
    @Schema(description = "The amount of diamond", example = "100")
    private Long diamond;
    @Schema(description = "The amount of emerald", example = "100")
    private Long emerald;
    @Schema(description = "The amount of amethyst", example = "100")
    private Long amethyst;
}
