package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheProperties {
    private boolean enabled;
    private RedisProperties redisProperties;
    private CacheExpirationProperties cacheExpirationProperties;
}
