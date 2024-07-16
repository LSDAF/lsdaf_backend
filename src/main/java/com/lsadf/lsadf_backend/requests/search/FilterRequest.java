package com.lsadf.lsadf_backend.requests.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    private String filterType;
    private String filterValue;
}
