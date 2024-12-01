package com.lsadf.core.requests.common;

import static com.lsadf.core.constants.JsonAttributes.Filter.TYPE;
import static com.lsadf.core.constants.JsonAttributes.Filter.VALUE;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter implements Serializable {

  @Serial private static final long serialVersionUID = -9160944941964752810L;

  @Schema(description = "Type of filter", example = "name")
  @JsonProperty(value = TYPE)
  @NotNull
  private String type;

  @Schema(description = "Value of filter", example = "toto")
  @JsonProperty(value = VALUE)
  @NotNull
  private String value;
}
