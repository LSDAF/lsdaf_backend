package com.lsadf.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakProperties {
    private String adminUri;
    private String uri;
    private String realm;
    private String clientId;
    private String clientSecret;
}
