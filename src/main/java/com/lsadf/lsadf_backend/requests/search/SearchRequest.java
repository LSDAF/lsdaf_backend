package com.lsadf.lsadf_backend.requests.search;

import com.lsadf.lsadf_backend.requests.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private List<FilterRequest> filters;
    private OrderBy orderBy;
}
