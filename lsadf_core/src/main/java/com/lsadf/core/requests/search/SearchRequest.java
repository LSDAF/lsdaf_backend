package com.lsadf.core.requests.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.requests.Request;
import com.lsadf.core.requests.common.Filter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

import static com.lsadf.core.constants.JsonAttributes.SearchRequest.FILTERS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest implements Request {

    @Serial
    private static final long serialVersionUID = 573371570502433749L;

    @JsonProperty(value = FILTERS)
    @Valid
    private List<Filter> filters;
}