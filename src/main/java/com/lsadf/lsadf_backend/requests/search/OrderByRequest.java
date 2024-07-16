package com.lsadf.lsadf_backend.requests.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderByRequest {
    private String orderByType;
    private String orderByValue;
}
