package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.AuthController;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.http.UnauthorizedException;
import com.lsadf.lsadf_backend.http_clients.KeycloakClient;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.properties.KeycloakProperties;
import com.lsadf.lsadf_backend.requests.user.UserLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserRefreshLoginRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;


/**
 * Implementation of the Auth Controller
 */
@RestController
@Slf4j
public class AuthControllerImpl extends BaseController implements AuthController {

    private final KeycloakProperties keycloakProperties;
    private final KeycloakClient keycloakClient;

    public AuthControllerImpl(KeycloakClient keycloakClient,
                              KeycloakProperties keycloakProperties) {
        this.keycloakClient = keycloakClient;
        this.keycloakProperties = keycloakProperties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> login() {
        try {
            log.info("Anonymous user wants to login with grant_type=authorization_code");
            HttpHeaders headers = new HttpHeaders();
            String newUrl = "http://localhost:8081/realms/LSADF/protocol/openid-connect/auth?client_id=lsadf-api&response_type=code&scope=openid&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Foauth2%2Fcallback";
            headers.setLocation(URI.create(newUrl));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (Exception e) {
            log.error("Could not login user", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not login user!", null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<JwtAuthentication>> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        try {

            log.info("User {} wants to login with grant_type=password", userLoginRequest.getUsername());

            String clientId = keycloakProperties.getClientId();
            String clientSecret = keycloakProperties.getClientSecret();

            String bodyString = "grant_type=password" +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&username=" + userLoginRequest.getUsername() +
                    "&password=" + userLoginRequest.getPassword();

            JwtAuthentication jwt = keycloakClient.getToken(keycloakProperties.getRealm(), bodyString);

            log.info("Received token: {}", jwt);
            return generateResponse(HttpStatus.OK, jwt);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception {} while getting access token: ", e.getClass(), e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception " + e.getClass() + " while getting access token: ", null);
        } catch (NotFoundException e) {
            log.error("Not found exception {} while getting access token: ", e.getClass(), e);
            return generateResponse(HttpStatus.NOT_FOUND, "Not found exception " + e.getClass() + " while getting access token: ", null);
        } catch (Exception e) {
            log.error("Exception {} while getting access token: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting access token: ", null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<JwtAuthentication>> refresh(@RequestBody @Valid UserRefreshLoginRequest userRefreshLoginRequest) {
        try {
            log.info("Anonymous user wants to login with grant_type=refresh_token");

            String clientId = keycloakProperties.getClientId();
            String clientSecret = keycloakProperties.getClientSecret();

            String bodyString = "grant_type=refresh_token" +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&refresh_token=" + userRefreshLoginRequest.getRefreshToken();

            JwtAuthentication response = keycloakClient.getToken(keycloakProperties.getRealm(), bodyString);

            log.info("Received token: {}", response);
            return generateResponse(HttpStatus.OK, response);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception {} while getting access token: ", e.getClass(), e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception " + e.getClass() + " while getting access token: " + e, null);
        } catch (NotFoundException e) {
            log.error("Not found exception {} while getting access token: " + e, e.getClass(), null);
            return generateResponse(HttpStatus.NOT_FOUND, "Not found exception " + e.getClass() + " while getting access token: " + e, null);
        } catch (Exception e) {
            log.error("Exception {} while getting access token: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting access token: " + e, null);
        }
    }
}
