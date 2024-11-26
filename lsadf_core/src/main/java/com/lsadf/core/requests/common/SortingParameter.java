package com.lsadf.core.requests.common;

import org.springframework.data.domain.Sort;

import java.util.Comparator;

public interface SortingParameter<T> {
    String getFieldName();

    Sort.Direction getDirection();

    Comparator<T> getComparator();
}
