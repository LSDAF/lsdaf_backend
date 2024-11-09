package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheExpirationProperties {
    private int characteristicsExpirationSeconds;
    private int currencyExpirationSeconds;
    private int stageExpirationSeconds;
    private int gameSaveOwnershipExpirationSeconds;
    private int localUserExpirationSeconds;
}
