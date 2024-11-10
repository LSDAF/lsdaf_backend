package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.lsadf_backend.constants.JsonViews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Inventory.ITEMS;

@NoArgsConstructor
//@AllArgsConstructor
@Schema(name = "Item", description = "Item object")
@Data
@Builder
@JsonPropertyOrder({ITEMS})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public class Item implements Model {

    @Serial
    private static final long serialVersionUID = 33494087785391763L;


}
