package com.lsadf.lsadf_backend.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType {
    BOOTS("boots"),
    CHESTPLATE("chestplate"),
    GLOVES("gloves"),
    HELMET("helmet"),
    SHIELD("shield"),
    SWORD("sword");

    private final String type;
}
