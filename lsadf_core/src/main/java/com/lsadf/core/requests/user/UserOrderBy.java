package com.lsadf.core.requests.user;

import com.lsadf.core.constants.JsonAttributes;
import com.lsadf.core.constants.SortingOrderParameter;
import com.lsadf.core.requests.common.OrderBy;

public enum UserOrderBy implements OrderBy {
  ID(JsonAttributes.ID, SortingOrderParameter.ASCENDING),
  ID_DESC(JsonAttributes.ID, SortingOrderParameter.DESCENDING),
  USERNAME(JsonAttributes.User.USERNAME, SortingOrderParameter.ASCENDING),
  USERNAME_DESC(JsonAttributes.User.USERNAME, SortingOrderParameter.DESCENDING),
  FIRST_NAME(JsonAttributes.User.FIRST_NAME, SortingOrderParameter.ASCENDING),
  FIRST_NAME_DESC(JsonAttributes.User.FIRST_NAME, SortingOrderParameter.DESCENDING),
  LAST_NAME(JsonAttributes.User.LAST_NAME, SortingOrderParameter.ASCENDING),
  LAST_NAME_DESC(JsonAttributes.User.LAST_NAME, SortingOrderParameter.DESCENDING),
  CREATED_AT(JsonAttributes.CREATED_AT, SortingOrderParameter.ASCENDING),
  CREATED_AT_DESC(JsonAttributes.CREATED_AT, SortingOrderParameter.DESCENDING),
  NONE(null, null);

  UserOrderBy(String fieldName, SortingOrderParameter order) {
    this.fieldName = fieldName;
    this.order = order;
  }

  private final String fieldName;
  private final SortingOrderParameter order;

  @Override
  public String getFieldName() {
    return this.fieldName;
  }

  @Override
  public SortingOrderParameter getOrder() {
    return this.order;
  }
}
