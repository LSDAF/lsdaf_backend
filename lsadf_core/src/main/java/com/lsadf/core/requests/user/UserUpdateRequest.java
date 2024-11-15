package com.lsadf.core.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.requests.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.core.constants.JsonAttributes.User.FIRST_NAME;
import static com.lsadf.core.constants.JsonAttributes.User.LAST_NAME;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateRequest implements Request {

    @Serial
    private static final long serialVersionUID = 3391683431995156829L;

    @JsonProperty(value = FIRST_NAME)
    @NotBlank
    private String firstName;

    @JsonProperty(value = LAST_NAME)
    @NotBlank
    private String lastName;
}
