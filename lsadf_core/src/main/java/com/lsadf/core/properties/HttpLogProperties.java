package com.lsadf.core.properties;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpLogProperties {
  private boolean enabled;
  private boolean colorEnabled;
  private List<HttpMethod> loggedMethods;
}
