package com.lsadf.lsadf_backend.requests.common;

import com.lsadf.core.constants.SortingOrderParameter;

public interface OrderBy {
    String getFieldName();

    SortingOrderParameter getOrder();
}
