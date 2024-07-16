package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthentication {
    @JsonProperty(JsonAttributes.JwtAuthentication.ACCESS_TOKEN)
    private final String accessToken;

    @JsonProperty(JsonAttributes.JwtAuthentication.USER_INFO)
    private final UserInfo userInfo;
}
