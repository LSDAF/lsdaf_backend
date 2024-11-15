package com.lsadf.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheProperties {
    private RedisProperties redisProperties;
    private CacheExpirationProperties cacheExpirationProperties;
}
