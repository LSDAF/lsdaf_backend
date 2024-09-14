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

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Gold.AMOUNT;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.Gold.ID;

@Data
@Schema(name = "Gold", description = "Gold amount Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ID, AMOUNT})
public class Gold {
    @JsonProperty(value = ID)
    @Schema(description = "Id of the game save", example = "9cbfec92-d7ab-496d-adbd-f55fc14d8e8c")
    private String id;

    @JsonProperty(value = AMOUNT)
    @PositiveOrZero
    @Schema(description = "Gold amount", example = "26093")
    private Long amount;
}
