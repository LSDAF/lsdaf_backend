package com.lsadf.controllers.impl;

import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.controllers.OAuth2Controller;
import com.lsadf.core.http_clients.KeycloakClient;
import com.lsadf.core.models.JwtAuthentication;
import com.lsadf.core.properties.KeycloakProperties;
import com.lsadf.core.properties.ServerProperties;
import com.lsadf.core.responses.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;

@RestController
@Slf4j
public class OAuth2ControllerImpl extends BaseController implements OAuth2Controller {

    private final KeycloakClient keycloakClient;
    private final KeycloakProperties keycloakProperties;
    private final ServerProperties serverProperties;

    public OAuth2ControllerImpl(KeycloakClient keycloakClient,
                                KeycloakProperties keycloakProperties,
                                ServerProperties serverProperties) {
        this.keycloakClient = keycloakClient;
        this.serverProperties = serverProperties;
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    public ResponseEntity<GenericResponse<JwtAuthentication>> handleOAuth2Callback(@RequestParam(CODE) String code) {
        // Handle the code returned from Keycloak here
        log.info("Received code: {}", code);
        // request token to keycloak using code

        String clientId = keycloakProperties.getClientId();
        String clientSecret = keycloakProperties.getClientSecret();
        String redirectUri = serverProperties.isHttps() ? "https://" : "http://" + serverProperties.getHostName() + ":" + serverProperties.getPort() + ControllerConstants.OAUTH2 + ControllerConstants.OAuth2.CALLBACK;

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
