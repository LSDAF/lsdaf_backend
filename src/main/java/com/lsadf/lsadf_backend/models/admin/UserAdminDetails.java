package com.lsadf.lsadf_backend.models.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.models.GameSave;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.UserAdminDetails.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ID, NAME, EMAIL, PASSWORD, ENABLED, PROVIDER, CREATED_AT, UPDATED_AT})
public class UserAdminDetails {
    @JsonProperty(value = ID)
    @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private String id;

    @JsonProperty(value = NAME)
    @Schema(description = "User name", example = "Toto Ducoin")
    private String name;

    @JsonProperty(value = EMAIL)
    @Schema(description = "User email", example = "toto@toto.com")
    private String email;

    @JsonProperty(value = PASSWORD)
    @Schema(description = "User password", example = "k127F978")
    private String password;

    @JsonProperty(value = ENABLED)
    @Schema(description = "User account is enabled", example = "true")
    private Boolean enabled;

    @JsonProperty(value = VERIFIED)
    @Schema(description = "User email is verified", example = "true")
    private Boolean verified;

    @JsonProperty(value = USER_ROLES)
    @Schema(description = "User roles", example = "[\"USER\"]")
    private List<UserRole> userRoles;

    @JsonProperty(value = PROVIDER)
    @Schema(description = "Social provider", example = "LOCAL")
    private SocialProvider socialProvider;

    @JsonProperty(value = CREATED_AT)
    @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
    private Date createdAt;

    @JsonProperty(value = UPDATED_AT)
    private Date updatedAt;

    @Schema(description = "Game saves of user")
    @JsonProperty(value = JsonAttributes.UserAdminDetails.GAME_SAVES)
    private List<GameSave> gameSaves;
}
