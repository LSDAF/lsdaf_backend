package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.properties.SwaggerContactProperties;
import com.lsadf.lsadf_backend.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for the Swagger.
 */
@Configuration
@Import({SwaggerContactProperties.class,
        SwaggerProperties.class})
public class SwaggerConfiguration {
    public static final String BEARER_AUTHENTICATION = "Bearer Authentication";
    public static final String OAUTH2_AUTHENTICATION = "OAuth2 Authentication";

    private static final String KEYCLOAK = "keycloak";

    private static final String BEARER = "bearer";
    private static final String JWT = "JWT";
    private static final String AUTHORIZATION = "Authorization";

    @ConfigurationProperties(prefix = "swagger")
    @Bean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @ConfigurationProperties(prefix = "swagger.contact")
    @Bean
    public SwaggerContactProperties swaggerContactProperties() {
        return new SwaggerContactProperties();
    }

    @Bean
    public OpenAPI openAPI(SwaggerProperties swaggerProperties) {
        Info info = new Info();

        info.setContact(buildContact(swaggerProperties.getContact()));
        info.setDescription(swaggerProperties.getDescription());
        info.setTitle(swaggerProperties.getTitle());


        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTHENTICATION))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTHENTICATION, createApiKeyScheme()))
                .info(info);
    }

    private SecurityScheme createApiKeyScheme() {
        return new SecurityScheme()
                .name(AUTHORIZATION)
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat(JWT)
                .scheme(BEARER);
    }


    private Contact buildContact(SwaggerContactProperties contactProperties) {
        Contact contact = new Contact();

        contact.setName(contactProperties.getName());
        contact.setEmail(contactProperties.getEmail());
        contact.setUrl(contactProperties.getUrl());

        return contact;
    }
}
