package com.lsadf.lsadf_backend.configurations.http_clients;

import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import static com.lsadf.core.constants.HttpClientTypes.KEYCLOAK_ADMIN;

public class KeycloakAdminFeignConfiguration extends CommonFeignConfiguration {

    @Bean
    public RequestInterceptor oauth2AccessTokenInterceptor(
            OAuth2AuthorizedClientManager authorizedClientManager) {
        return new OAuth2AccessTokenInterceptor(KEYCLOAK_ADMIN, authorizedClientManager);
    }
}
