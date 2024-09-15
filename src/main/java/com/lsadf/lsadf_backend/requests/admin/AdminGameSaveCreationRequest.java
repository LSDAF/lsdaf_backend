package com.lsadf.lsadf_backend.requests.admin;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

/**
 * Request for creating a new game save
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGameSaveCreationRequest {
    @Nullable
    private String id;

    @Email
    @Nullable
    private String userEmail;

    @PositiveOrZero
    private long gold;

    @PositiveOrZero
    private long diamond;

    @PositiveOrZero
    private long emerald;

    @PositiveOrZero
    private long amethyst;

    @Positive
    private long healthPoints;

    @Positive
    private long attack;
}
