package com.lsadf.lsadf_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Data
@AllArgsConstructor
public class JwtAuthentication {
    private final String accessToken;
    private final UserInfo userInfo;
}
