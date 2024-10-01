package com.lsadf.lsadf_backend.requests.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.lsadf_backend.requests.Request;
import com.lsadf.lsadf_backend.requests.common.Filter;
import com.lsadf.lsadf_backend.requests.common.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.SearchRequest.FILTERS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest implements Request {

    @Serial
    private static final long serialVersionUID = 573371570502433749L;

    @JsonProperty(value = FILTERS)
    private List<Filter> filters;
}
