package com.lsadf.lsadf_backend.requests.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminGameSaveCreationRequest {
    private String id;
    private String userEmail;
    private long gold;
    private long healthPoints;
    private long attack;
}
