package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.lsadf_backend.constants.JsonViews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Set;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Inventory.ITEMS;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Inventory", description = "Inventory object")
@Data
@Builder
@JsonPropertyOrder({ITEMS})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public class Inventory implements Model {

    @Serial
    private static final long serialVersionUID = 33494087785391763L;

    @JsonView(JsonViews.External.class)
    @JsonProperty(value = ITEMS)
    @Schema(description = "List of items in the inventory", example = "[\"item1\", \"item2\"]")
    private Set<Item> items;
}
