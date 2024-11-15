package com.lsadf.lsadf_backend.requests.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.ItemType;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Item.ITEM_TYPE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ITEM_TYPE})
public class ItemRequest implements Request {

    @Serial
    private static final long serialVersionUID = -1116418739363127022L;

    @Schema(description = "Item type", example = "boots")
    @JsonProperty(value = ITEM_TYPE)
    @NotNull
    private String itemType;
}
