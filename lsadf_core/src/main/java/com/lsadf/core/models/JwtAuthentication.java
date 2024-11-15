package com.lsadf.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;

import static com.lsadf.core.constants.JsonAttributes.JwtAuthentication.*;


@Data
@AllArgsConstructor
@JsonPropertyOrder({ACCESS_TOKEN, EXPIRES_IN, REFRESH_TOKEN, REFRESH_EXPIRES_IN})
public class JwtAuthentication implements Model {

    @Serial
    private static final long serialVersionUID = -5360094704215801310L;

    @JsonProperty(value = ACCESS_TOKEN)
    private final String accessToken;

    @JsonProperty(value = EXPIRES_IN)
    private final Long expiresIn;

    @JsonProperty(value = REFRESH_TOKEN)
    private final String refreshToken;

    @JsonProperty(value = REFRESH_EXPIRES_IN)
    private final Long refreshExpiresIn;
}
