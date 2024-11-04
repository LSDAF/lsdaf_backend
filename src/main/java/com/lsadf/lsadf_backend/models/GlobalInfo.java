package com.lsadf.lsadf_backend.models;

import lombok.*;

import java.io.Serial;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalInfo implements Model {

    @Serial
    private static final long serialVersionUID = -5539057784012769955L;

    private Instant now;
    private Long gameSaveCounter;
    private Long userCounter;
}
