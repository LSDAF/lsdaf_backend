package com.lsadf.lsadf_backend.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.requests.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.NAME;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateRequest implements Request {

    @Serial
    private static final long serialVersionUID = 3391683431995156829L;

    @JsonProperty(value = NAME)
    @NotBlank
    private String name;
}
