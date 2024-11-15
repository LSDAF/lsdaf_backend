package com.lsadf.core.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

import static com.lsadf.core.constants.JsonAttributes.User.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminUserUpdateRequest implements Request {
    @Serial
    private static final long serialVersionUID = -4515896456126778133L;

    @NotBlank
    @Schema(description = "Name of user to update", example = "Toto Dupont")
    @JsonProperty(value = FIRST_NAME)
    private String firstName;

    @NotBlank
    @Schema(description = "Lastname of user to update", example = "Dupont")
    @JsonProperty(value = LAST_NAME)
    private String lastName;

    @Schema(description = "Verified status of user to update", example = "true")
    @JsonProperty(value = EMAIL_VERIFIED)
    @NotNull
    private Boolean emailVerified;

    @Schema(description = "Enabled status of user to update", example = "true")
    @JsonProperty(value = ENABLED)
    @NotNull
    private Boolean enabled;

    @Schema(description = "Roles of user to update", example = "[\"USER\"]")
    @JsonProperty(value = USER_ROLES)
    private List<String> userRoles;
}
