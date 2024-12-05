package com.lsadf.core.models;

import static com.lsadf.core.constants.JsonAttributes.ItemStat.BASE_VALUE;
import static com.lsadf.core.constants.JsonAttributes.ItemStat.STATISTIC;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.constants.JsonViews;
import com.lsadf.core.constants.item.ItemStatistic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class ItemStat implements Model {
  @JsonView(JsonViews.External.class)
  @JsonProperty(value = STATISTIC)
  @Schema(description = "Item stat statistic", example = "ATTACK")
  private ItemStatistic statistic;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = BASE_VALUE)
  @Schema(description = "Item stat base value", example = "100.0")
  @Positive
  private Float baseValue;
}
