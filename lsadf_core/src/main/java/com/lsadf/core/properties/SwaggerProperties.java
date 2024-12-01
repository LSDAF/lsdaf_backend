package com.lsadf.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Properties for configuring Swagger */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwaggerProperties {
  private String title;
  private String description;
  private String version;
  private SwaggerContactProperties contact;
}
