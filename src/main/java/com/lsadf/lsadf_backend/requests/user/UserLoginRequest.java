package com.lsadf.lsadf_backend.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.EMAIL;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.PASSWORD;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4723986067784943176L;

    @NotBlank
    @JsonProperty(value = EMAIL)
    @Email
    private String email;

    @NotBlank
    @JsonProperty(value = PASSWORD)
    private String password;
}
