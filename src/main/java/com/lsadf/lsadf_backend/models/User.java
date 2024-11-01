package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lsadf.lsadf_backend.constants.JsonViews;
import com.lsadf.lsadf_backend.serializers.DateToLongSerializer;
import com.lsadf.lsadf_backend.serializers.LongToDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Date;
import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.ID;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

/**
 * User DTO
 */
@SuperBuilder
@Data
@AllArgsConstructor
public class User implements Model {

    @Serial
    private static final long serialVersionUID = 144315795668992686L;

    @JsonView(JsonViews.Admin.class)
    @JsonProperty(value = ID)
    @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private final String id;

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = FIRST_NAME)
    @Schema(description = "First name", example = "Toto")
    private String firstName;

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = LAST_NAME)
    @Schema(description = "Last name", example = "TUTU")
    private String lastName;

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = USERNAME)
    @Schema(description = "User username", example = "toto@toto.com")
    private final String username;

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = ENABLED)
    @Schema(description = "User enabled", example = "true")
    private boolean enabled;

    @JsonView(JsonViews.Internal.class)
    @JsonProperty(value = EMAIL_VERIFIED)
    @Schema(description = "Email verified", example = "true")
    private boolean emailVerified;

    @JsonView(JsonViews.Admin.class)
    @JsonProperty(value = USER_ROLES)
    @Schema(description = "User roles", example = "[\"USER\"]")
    private List<String> userRoles;

    @JsonView(JsonViews.Admin.class)
    @JsonProperty(value = CREATED_TIMESTAMP)
    @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
    @JsonSerialize(using = DateToLongSerializer.class)
    @JsonDeserialize(using = LongToDateSerializer.class)
    private final Date createdTimestamp;
}
