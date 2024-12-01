package com.lsadf.lsadf_backend.controllers.impl;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;
import static com.lsadf.core.utils.TokenUtils.*;

import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.core.models.UserInfo;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.utils.TokenUtils;
import com.lsadf.lsadf_backend.controllers.UserController;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the User Controller */
@RestController
@Slf4j
public class UserControllerImpl extends BaseController implements UserController {
  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<UserInfo>> getUserInfo(Jwt jwt) {
    String username = getUsernameFromJwt(jwt);
    String name = getNameFromJwt(jwt);
    boolean verified = getEmailVerifiedFromJwt(jwt);
    List<GrantedAuthority> authorities = TokenUtils.getRolesFromJwt(jwt);
    Set<String> roles =
        authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

    UserInfo userInfo = new UserInfo(name, username, verified, roles);
    return generateResponse(HttpStatus.OK, userInfo);
  }
}
