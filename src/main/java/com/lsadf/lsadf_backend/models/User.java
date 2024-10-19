package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lsadf.lsadf_backend.serializers.DateToLongSerializer;
import com.lsadf.lsadf_backend.serializers.LongToDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.util.Date;
import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

/**
 * User DTO
 */
@Builder
@Data
@AllArgsConstructor
@JsonPropertyOrder({ID, FIRST_NAME, LAST_NAME, USERNAME, ENABLED, EMAIL_VERIFIED, USER_ROLES, CREATION_TIMESTAMP})
public class User implements Model {

    @Serial
    private static final long serialVersionUID = 144315795668992686L;

    @JsonProperty(value = ID)
    @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private final String id;

    @JsonProperty(value = FIRST_NAME)
    @Schema(description = "First name", example = "Toto")
    private String firstName;

    @JsonProperty(value = LAST_NAME)
    @Schema(description = "Last name", example = "TUTU")
    private String lastName;

    @JsonProperty(value = USERNAME)
    @Schema(description = "User username", example = "toto@toto.com")
    private final String username;

    @JsonProperty(value = ENABLED)
    @Schema(description = "User enabled", example = "true")
    private boolean enabled;

    @JsonProperty(value = EMAIL_VERIFIED)
    @Schema(description = "Email verified", example = "true")
    private boolean emailVerified;

    @JsonProperty(value = USER_ROLES)
    @Schema(description = "User roles", example = "[\"USER\"]")
    private List<String> userRoles;

    @JsonProperty(value = CREATION_TIMESTAMP)
    @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
    @JsonSerialize(using = DateToLongSerializer.class)
    @JsonDeserialize(using = LongToDateSerializer.class)
    private final Date creationTimestamp;

}
