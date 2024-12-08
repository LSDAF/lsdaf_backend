package com.lsadf.core.requests.item;

import static com.lsadf.core.constants.JsonAttributes.Item.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.models.ItemStat;
import com.lsadf.core.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
  CLIENT_ID,
  TYPE,
  BLUEPRINT_ID,
  RARITY,
  IS_EQUIPPED,
  LEVEL,
  MAIN_STAT,
  ADDITIONAL_STATS
})
public class ItemRequest implements Request {

  @Serial private static final long serialVersionUID = -1116418739363127022L;

  @Schema(
      description = "Client generated id, concatenation of inventory id and item id",
      example = "36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111")
  @JsonProperty(value = CLIENT_ID)
  @NotNull
  private String clientId;

  @Schema(description = "Item type", example = "boots")
  @JsonProperty(value = TYPE)
  @NotNull
  private String itemType;

  @Schema(description = "Blueprint id", example = "blueprint_id")
  @JsonProperty(value = BLUEPRINT_ID)
  @NotNull
  private String blueprintId;

  @Schema(description = "Item rarity", example = "LEGENDARY")
  @JsonProperty(value = RARITY)
  @NotNull
  private String itemRarity;

  @Schema(description = "Is equipped", example = "true")
  @JsonProperty(value = IS_EQUIPPED)
  @NotNull
  private Boolean isEquipped;

  @Schema(description = "Item level", example = "20")
  @JsonProperty(value = LEVEL)
  @NotNull
  private Integer level;

  @Schema(description = "Main item stat")
  @JsonProperty(value = MAIN_STAT)
  @NotNull
  private ItemStat mainStat;

  @Schema(description = "Additional item stat list")
  @JsonProperty(value = ADDITIONAL_STATS)
  @NotNull
  private List<ItemStat> additionalStats;
}
