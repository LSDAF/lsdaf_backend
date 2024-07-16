package com.lsadf.lsadf_backend.requests;

import com.lsadf.lsadf_backend.constants.JsonAttributes;
import com.lsadf.lsadf_backend.constants.SortingOrderParameter;

public enum UserOrderBy implements OrderBy {
    ID(JsonAttributes.ID, SortingOrderParameter.ASCENDING),
    ID_DESC(JsonAttributes.ID, SortingOrderParameter.DESCENDING),
    EMAIL(JsonAttributes.User.EMAIL, SortingOrderParameter.ASCENDING),
    EMAIL_DESC(JsonAttributes.User.EMAIL, SortingOrderParameter.DESCENDING),
    NAME(JsonAttributes.User.NAME, SortingOrderParameter.ASCENDING),
    NAME_DESC(JsonAttributes.User.NAME, SortingOrderParameter.DESCENDING),
    CREATED_AT(JsonAttributes.CREATED_AT, SortingOrderParameter.ASCENDING),
    CREATED_AT_DESC(JsonAttributes.CREATED_AT, SortingOrderParameter.DESCENDING),
    UPDATED_AT(JsonAttributes.UPDATED_AT, SortingOrderParameter.ASCENDING),
    UPDATED_AT_DESC(JsonAttributes.UPDATED_AT, SortingOrderParameter.DESCENDING);

    UserOrderBy(String fieldName,
                        SortingOrderParameter order) {
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
