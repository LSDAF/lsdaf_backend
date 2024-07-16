package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

/**
 * User DTO
 */
@Builder
@Data
@AllArgsConstructor
@JsonPropertyOrder({ID, NAME, EMAIL, PROVIDER, CREATED_AT, UPDATED_AT})
public class User {
    @JsonProperty(value = ID)
    @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private final String id;

    @JsonProperty(value = NAME)
    @Schema(description = "User name", example = "Toto Ducoin")
    private final String name;

    @JsonProperty(value = EMAIL)
    @Schema(description = "User email", example = "toto@toto.com")
    private final String email;

    @JsonIgnore
    private final String password;

    @JsonProperty(value = PROVIDER)
    private final String socialProvider;

    @JsonProperty(value = CREATED_AT)
    @Schema(description = "Creation date", example = "2022-01-01T00:00:00.000Z")
    private final Date createdAt;

    @JsonProperty(value = UPDATED_AT)
    @Schema(description = "Update date", example = "2022-01-01T00:00:00.000Z")
    private final Date updatedAt;
}
