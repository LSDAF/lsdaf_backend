package com.lsadf.lsadf_backend.configurations.keycloak;

import com.lsadf.lsadf_backend.properties.KeycloakAdminProperties;
import com.lsadf.lsadf_backend.properties.KeycloakProperties;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {

    @Bean
    public Keycloak keycloak(KeycloakProperties keycloakProperties,
                             KeycloakAdminProperties keycloakAdminProperties) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getUri())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakAdminProperties.getClientId())
                .clientSecret(keycloakAdminProperties.getClientSecret())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
