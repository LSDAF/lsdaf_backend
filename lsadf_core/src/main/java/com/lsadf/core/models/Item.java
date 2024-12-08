package com.lsadf.core.models;

import static com.lsadf.core.constants.JsonAttributes.Inventory.ITEMS;
import static com.lsadf.core.constants.JsonAttributes.Item.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.constants.JsonAttributes;
import com.lsadf.core.constants.JsonViews;
import com.lsadf.core.constants.item.ItemRarity;
import com.lsadf.core.constants.item.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Item", description = "Item object")
@Data
@Builder
@JsonPropertyOrder({ITEMS})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public class Item implements Model {

  @Serial private static final long serialVersionUID = 6615198748250122221L;

  @JsonView(JsonViews.Admin.class)
  @JsonProperty(value = JsonAttributes.ID)
  @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
  private String id;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = BLUEPRINT_ID)
  @Schema(description = "Blueprint id", example = "blueprint_id")
  private String blueprintId;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = TYPE)
  @Schema(description = "Item type", example = "BOOTS")
  private ItemType itemType;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = RARITY)
  @Schema(description = "Item rarity", example = "LEGENDARY")
  private ItemRarity itemRarity;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = IS_EQUIPPED)
  @Schema(description = "Is Equipped", example = "true")
  private Boolean isEquipped;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = LEVEL)
  @Schema(description = "Item level", example = "20")
  private Integer level;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = MAIN_STAT)
  @Schema(description = "Main item stat")
  @Embedded
  private ItemStat mainStat;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = ADDITIONAL_STATS)
  @Schema(description = "Additional item stat list")
  @ElementCollection
  private List<ItemStat> additionalStats;
}
