package com.lsadf.lsadf_backend.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.requests.Request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.JwtAuthentication.REFRESH_TOKEN;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.EMAIL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRefreshLoginRequest implements Request {

    @Serial
    private static final long serialVersionUID = -1758378448778560290L;

    @JsonProperty(value = EMAIL)
    @NotBlank
    @Email
    private String email;

    @JsonProperty(value = REFRESH_TOKEN)
    @NotBlank
    private String refreshToken;
}
