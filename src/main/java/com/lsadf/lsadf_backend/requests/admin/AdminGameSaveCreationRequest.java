package com.lsadf.lsadf_backend.requests.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminGameSaveCreationRequest {
    private String userId;
    private int gold;
    private int healthPoints;
    private int attack;
}
