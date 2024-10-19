package com.lsadf.lsadf_backend.requests.admin;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserCreationRequest implements Request {

    @Serial
    private static final long serialVersionUID = 9104893581644308116L;

    @NotBlank
    @Schema(description = "Name of user to create", example = "Toto Dupont")
    @JsonProperty(value = FIRST_NAME)
    private String firstName;

    @NotBlank
    @Schema(description = "Lastname of user to create", example = "Dupont")
    @JsonProperty(value = LAST_NAME)
    private String lastName;

    @Schema(description = "User id of user to create. Can be null", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    @JsonProperty(value = USER_ID)
    private String userId;

    @Schema(description = "Enabled status of user to create", example = "true")
    @JsonProperty(value = ENABLED)
    private Boolean enabled;

    @Schema(description = "Verified email status of user to create", example = "true")
    @JsonProperty(value = EMAIL_VERIFIED)
    private Boolean emailVerified;

    @Email
    @NotBlank
    @Schema(description = "Username of user to create", example = "toto@toto.fr")
    @JsonProperty(value = USERNAME)
    private String username;
}
