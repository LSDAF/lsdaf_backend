package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheExpirationProperties {
    private long currencyExpirationSeconds;
    private long gameSaveOwnershipExpirationSeconds;
    private long localUserExpirationSeconds;
}
