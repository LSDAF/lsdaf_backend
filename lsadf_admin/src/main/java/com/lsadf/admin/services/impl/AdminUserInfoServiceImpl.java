package com.lsadf.admin.services.impl;

import com.lsadf.admin.services.AdminUserInfoService;
import com.lsadf.core.models.UserInfo;
import com.lsadf.core.utils.TokenUtils;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/** Implementation of the Admin User Info Service */
@BrowserCallable
public class AdminUserInfoServiceImpl implements AdminUserInfoService {
  /** {@inheritDoc} */
  @Override
  public UserInfo getUserInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal();
    return TokenUtils.getUserInfoFromJwt(jwt);
  }
}
