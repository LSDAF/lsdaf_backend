package com.lsadf.lsadf_backend.requests.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.requests.Request;
import com.lsadf.lsadf_backend.requests.item.ItemRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.Inventory.ITEMS;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ITEMS})
public class InventoryRequest implements Request {

    @Serial
    private static final long serialVersionUID = 33494087785391763L;

    @JsonProperty(value = ITEMS)
    @NotNull
    private List<ItemRequest> items;
}
