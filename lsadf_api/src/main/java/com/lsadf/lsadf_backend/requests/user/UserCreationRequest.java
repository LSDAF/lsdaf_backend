package com.lsadf.lsadf_backend.requests.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

import static com.lsadf.core.constants.JsonAttributes.User.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationRequest implements Request {

    @Serial
    private static final long serialVersionUID = 7976141604912528826L;

    @NotBlank
    @Schema(description = "Name of user to create", example = "Toto Dupont")
    @JsonProperty(value = FIRST_NAME)
    private String firstName;

    @NotBlank
    @Schema(description = "Lastname of user to create", example = "Dupont")
    @JsonProperty(value = LAST_NAME)
    private String lastName;

    @Size(min = 8)
    @Schema(description = "Password of user to create", example = "k127F978")
    @JsonProperty(value = PASSWORD)
    private String password;

    @Email
    @NotBlank
    @Schema(description = "Username of user to create", example = "toto@toto.fr")
    @JsonProperty(value = USERNAME)
    private String username;

    @JsonIgnore
    @Builder.Default
    private boolean enabled = true;

    @JsonIgnore
    @Builder.Default
    private boolean emailVerified = false;

    @JsonIgnore
    private List<String> userRoles;
}
