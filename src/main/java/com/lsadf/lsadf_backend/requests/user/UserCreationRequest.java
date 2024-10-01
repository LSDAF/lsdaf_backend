package com.lsadf.lsadf_backend.requests.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationRequest implements Request {

    @Serial
    private static final long serialVersionUID = 7976141604912528826L;

    @JsonIgnore
    private String userId;

    @JsonIgnore
    private String providerUserId;

    @NotBlank
    @Schema(description = "Name of user to create", example = "Toto Dupont")
    @JsonProperty(value = NAME)
    private String name;

    @Size(min = 8)
    @Schema(description = "Password of user to create", example = "k127F978")
    @JsonProperty(value = PASSWORD)
    private String password;

    @Email
    @NotBlank
    @Schema(description = "Email of user to create", example = "toto@toto.fr")
    @JsonProperty(value = EMAIL)
    private String email;

    @JsonIgnore
    private List<UserRole> userRoles;

    @JsonIgnore
    @Builder.Default
    private SocialProvider socialProvider = SocialProvider.LOCAL;
}
