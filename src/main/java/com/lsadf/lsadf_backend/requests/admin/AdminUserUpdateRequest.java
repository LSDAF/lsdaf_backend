package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.constants.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminUserUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -4515896456126778133L;

    @NotBlank
    @Schema(description = "Name of user to update", example = "Toto Dupont")
    @JsonProperty(value = NAME)
    private String name;

    @Size(min = 8)
    @Schema(description = "Password of user to update", example = "k127F978")
    @JsonProperty(value = PASSWORD)
    private String password;

    @Email
    @NotBlank
    @Schema(description = "Email of user to update", example = "toto@toto.fr")
    @JsonProperty(value = EMAIL)
    private String email;

    @Schema(description = "Enabled status of user to update", example = "true")
    @JsonProperty(value = ENABLED)
    private Boolean enabled;

    @Schema(description = "Roles of user to update", example = "[\"USER\"]")
    @JsonProperty(value = USER_ROLES)
    private Set<UserRole> userRoles;
}
