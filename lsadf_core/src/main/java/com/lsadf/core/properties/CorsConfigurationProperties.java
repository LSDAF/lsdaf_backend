package com.lsadf.core.properties;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Properties for configuring CORS */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorsConfigurationProperties {
  private List<String> allowedOrigins;
  private List<String> allowedMethods;
  private List<String> allowedHeaders;
  private List<String> exposedHeaders;
  private Boolean allowCredentials;
}
