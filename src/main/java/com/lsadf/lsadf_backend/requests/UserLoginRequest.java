package com.lsadf.lsadf_backend.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4723986067784943176L;

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
