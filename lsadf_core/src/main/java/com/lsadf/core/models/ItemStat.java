package com.lsadf.core.models;

import com.lsadf.core.constants.item.ItemStatistic;
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
  private ItemStatistic statistic;
  @Positive private Float baseValue;
}
