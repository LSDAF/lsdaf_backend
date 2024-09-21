package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.JwtAuthentication.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.JwtAuthentication.USER_INFO;

@Data
@AllArgsConstructor
@JsonPropertyOrder({ACCESS_TOKEN, REFRESH_TOKEN, USER_INFO})
public class JwtAuthentication {
    @JsonProperty(value = ACCESS_TOKEN)
    private final String accessToken;

    @JsonProperty(value = REFRESH_TOKEN)
    private final String refreshToken;

    @JsonProperty(value = USER_INFO)
    private final UserInfo userInfo;
}
