package com.lsadf.lsadf_backend.requests.admin;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserCreationRequest implements Request {

    @Serial
    private static final long serialVersionUID = 9104893581644308116L;

    @NotBlank
    @Schema(description = "Name of user to create", example = "Toto Dupont")
    @JsonProperty(value = NAME)
    private String name;

    @Schema(description = "User id of user to create. Can be null", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    @JsonProperty(value = USER_ID)
    private String userId;

    @Schema(description = "Enabled status of user to create", example = "true")
    @JsonProperty(value = ENABLED)
    private Boolean enabled;

    @Schema(description = "Verified email status of user to create", example = "true")
    @JsonProperty(value = VERIFIED)
    private Boolean verified;

    @Email
    @NotBlank
    @Schema(description = "Email of user to create", example = "toto@toto.fr")
    @JsonProperty(value = EMAIL)
    private String email;

    @Size(min = 8)
    @Schema(description = "Password of user to create", example = "k127F978")
    @JsonProperty(value = PASSWORD)
    private String password;

    @Schema(description = "Social provider of user to create", example = "LOCAL")
    @JsonProperty(value = PROVIDER)
    private SocialProvider socialProvider;

    @Schema(description = "Roles of user to create", example = "[\"USER\"]")
    @JsonProperty(value = USER_ROLES)
    private List<UserRole> userRoles;

    @Schema(description = "Provider user id of user to create")
    @JsonProperty(value = PROVIDER_USER_ID)
    private String providerUserId;
}
