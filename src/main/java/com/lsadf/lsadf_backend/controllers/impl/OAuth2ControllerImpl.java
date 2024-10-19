package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.OAuth2Controller;
import com.lsadf.lsadf_backend.http_clients.KeycloakClient;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.properties.KeycloakProperties;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

@RestController
@Slf4j
public class OAuth2ControllerImpl extends BaseController implements OAuth2Controller {

    private final KeycloakClient keycloakClient;
    private final KeycloakProperties keycloakProperties;

    public OAuth2ControllerImpl(KeycloakClient keycloakClient,
                                KeycloakProperties keycloakProperties) {
        this.keycloakClient = keycloakClient;
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    public ResponseEntity<GenericResponse<JwtAuthentication>> handleOAuth2Callback(@RequestParam(CODE) String code) {
        // Handle the code returned from Keycloak here
        log.info("Received code: {}", code);
        // request token to keycloak using code

        String clientId = keycloakProperties.getClientId();
        String clientSecret = keycloakProperties.getClientSecret();
        String redirectUri = "http://localhost:8080/api/oauth2/callback";

        String bodyString = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code +
                "&redirect_uri=" + redirectUri;

        JwtAuthentication jwt = keycloakClient.getToken(keycloakProperties.getRealm(), bodyString);
        log.info("Received token: {}", jwt);
        if (jwt == null) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // return token
        return generateResponse(HttpStatus.OK, jwt);
    }
}
