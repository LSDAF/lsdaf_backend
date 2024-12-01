package com.lsadf.core.http_clients;

import com.lsadf.core.configurations.http_clients.CommonFeignConfiguration;
import com.lsadf.core.constants.HttpClientTypes;
import com.lsadf.core.models.JwtAuthentication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = HttpClientTypes.KEYCLOAK,
    configuration = CommonFeignConfiguration.class,
    primary = false)
public interface KeycloakClient {

  String REALM = "realm";
  String TOKEN_ENDPOINT = "/realms/{realm}/protocol/openid-connect/token";

  @PostMapping(
      path = TOKEN_ENDPOINT,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  JwtAuthentication getToken(@PathVariable(value = REALM) String realm, @RequestBody String body);
}
