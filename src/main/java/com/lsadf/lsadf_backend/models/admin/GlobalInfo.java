package com.lsadf.lsadf_backend.models.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalInfo {
    private Instant now;
    private Long gameSaveCounter;
    private Long userCounter;
}
