package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Properties for configuring the data source
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwaggerContactProperties {
    private String email;
    private String name;
    private String url;
}