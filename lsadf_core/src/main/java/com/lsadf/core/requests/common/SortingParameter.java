package com.lsadf.core.requests.common;

import java.util.Comparator;
import org.springframework.data.domain.Sort;

public interface SortingParameter<T> {
  String getFieldName();

  Sort.Direction getDirection();

  Comparator<T> getComparator();
}
