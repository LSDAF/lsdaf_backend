package com.lsadf.admin.services;

import com.lsadf.core.models.UserInfo;

/** Service for getting user info from the admin UI */
public interface AdminUserInfoService {

  /**
   * Get the user info from the current user
   * @return the user info
   */
  UserInfo getUserInfo();
}
