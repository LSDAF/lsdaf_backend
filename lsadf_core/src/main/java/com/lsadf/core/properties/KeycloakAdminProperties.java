package com.lsadf.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakAdminProperties {
  private String clientId;
  private String clientSecret;
}
