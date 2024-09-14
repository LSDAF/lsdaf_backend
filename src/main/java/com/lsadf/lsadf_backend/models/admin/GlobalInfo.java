package com.lsadf.lsadf_backend.models.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalInfo {
    private Long gameSaveCounter;
    private Long userCounter;
}
