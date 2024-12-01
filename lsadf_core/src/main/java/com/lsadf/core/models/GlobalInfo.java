package com.lsadf.core.models;

import java.io.Serial;
import java.time.Instant;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalInfo implements Model {

  @Serial private static final long serialVersionUID = -5539057784012769955L;

  private Instant now;
  private Long gameSaveCounter;
  private Long userCounter;
}
