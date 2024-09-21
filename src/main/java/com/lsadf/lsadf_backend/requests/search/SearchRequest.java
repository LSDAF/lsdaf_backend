package com.lsadf.lsadf_backend.requests.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.requests.common.Filter;
import com.lsadf.lsadf_backend.requests.common.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.SearchRequest.FILTERS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    @JsonProperty(value = FILTERS)
    private List<Filter> filters;
}
