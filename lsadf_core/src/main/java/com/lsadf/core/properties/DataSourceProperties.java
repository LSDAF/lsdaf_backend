package com.lsadf.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Properties for configuring the data source */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceProperties {
  private String url;
  private String username;
  private String password;
}
