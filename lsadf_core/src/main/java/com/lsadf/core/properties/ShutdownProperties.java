package com.lsadf.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShutdownProperties {
  private boolean flushCacheAtShutdown;
}
