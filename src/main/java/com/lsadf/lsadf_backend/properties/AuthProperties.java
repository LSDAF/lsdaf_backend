package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthProperties {
    private String tokenSecret;
    private long tokenExpirationMs;
    private String refreshTokenSecret;
    private long refreshTokenExpirationMs;
}
