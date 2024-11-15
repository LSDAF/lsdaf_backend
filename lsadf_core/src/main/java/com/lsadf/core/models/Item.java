package com.lsadf.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.constants.JsonAttributes;
import com.lsadf.core.constants.JsonViews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.core.constants.JsonAttributes.Inventory.ITEMS;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Item", description = "Item object")
@Data
@Builder
@JsonPropertyOrder({ITEMS})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public class Item implements Model {

    @Serial
    private static final long serialVersionUID = 6615198748250122221L;

    @JsonView(JsonViews.Admin.class)
    @JsonProperty(value = JsonAttributes.ID)
    @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
    private String id;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = ITEM_TYPE)
    @Schema(description = "Item type", example = "boots")
    private String itemType;
}
