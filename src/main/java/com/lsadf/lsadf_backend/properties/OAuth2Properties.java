package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Properties {
    private List<String> authorizedRedirectUris;
    private Map<String, OAuth2ClientProperties.Provider> provider;
    private Map<String, OAuth2ClientProperties.Registration> registration;
}
