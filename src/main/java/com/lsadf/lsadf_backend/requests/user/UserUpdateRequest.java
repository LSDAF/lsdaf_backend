package com.lsadf.lsadf_backend.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.NAME;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateRequest {

    @JsonProperty(value = NAME)
    @NotBlank
    private String name;
}
