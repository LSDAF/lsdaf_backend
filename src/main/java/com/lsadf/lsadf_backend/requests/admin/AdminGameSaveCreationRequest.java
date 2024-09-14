package com.lsadf.lsadf_backend.requests.admin;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminGameSaveCreationRequest {
    @Nullable
    private String id;

    @Email
    @Nullable
    private String userEmail;

    @PositiveOrZero
    private long gold;

    @Positive
    private long healthPoints;

    @Positive
    private long attack;
}
