package com.lsadf.core.requests.search;

import com.lsadf.core.requests.common.SearchFilter;

public enum UserSearchFilter implements SearchFilter {
  ID("id"),
  NAME("name"),
  EMAIL("email"),
  SOCIAL_PROVIDER("socialProvider");

  private final String filterType;

  UserSearchFilter(String filterType) {
    this.filterType = filterType;
  }

  @Override
  public String getFilerType() {
    return filterType;
  }
}
