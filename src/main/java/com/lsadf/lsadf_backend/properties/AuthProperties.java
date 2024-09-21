package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthProperties {
    private String tokenSecret;
    private int tokenExpirationSeconds;
    private String refreshTokenSecret;
    private int refreshTokenExpirationSeconds;
    private String invalidatedTokenCleanCron;
}
