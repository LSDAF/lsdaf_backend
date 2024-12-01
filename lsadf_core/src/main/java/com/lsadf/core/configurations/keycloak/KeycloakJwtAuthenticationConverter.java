package com.lsadf.core.configurations.keycloak;

import com.lsadf.core.utils.TokenUtils;
import java.util.Collection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class KeycloakJwtAuthenticationConverter
    implements Converter<Jwt, Collection<GrantedAuthority>> {

  private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter =
      new JwtGrantedAuthoritiesConverter();

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    // Extract realm_access roles
    Collection<GrantedAuthority> authorities = TokenUtils.getRolesFromJwt(jwt);

    // Add default authorities (like scope-based authorities)
    authorities.addAll(defaultGrantedAuthoritiesConverter.convert(jwt));

    return authorities;
  }
}
