package com.lsadf.lsadf_backend.requests;

import com.lsadf.lsadf_backend.constants.SortingOrderParameter;

public interface OrderBy {
    String getFieldName();
    SortingOrderParameter getOrder();
}
