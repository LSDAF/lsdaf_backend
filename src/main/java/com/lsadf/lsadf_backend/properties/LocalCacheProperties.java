package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalCacheProperties {
    private int localUserCacheMaxSize;
    private int invalidatedRefreshTokenCacheMaxSize;
}
