package com.lsadf.lsadf_backend.requests.game_save;

import com.lsadf.lsadf_backend.constants.JsonAttributes;
import com.lsadf.lsadf_backend.constants.SortingOrderParameter;
import com.lsadf.lsadf_backend.requests.common.OrderBy;

public enum GameSaveOrderBy implements OrderBy {
    CREATED_AT(JsonAttributes.CREATED_AT, SortingOrderParameter.ASCENDING),
    CREATED_AT_DESC(JsonAttributes.CREATED_AT, SortingOrderParameter.DESCENDING),
    UPDATED_AT(JsonAttributes.UPDATED_AT, SortingOrderParameter.ASCENDING),
    UPDATED_AT_DESC(JsonAttributes.UPDATED_AT, SortingOrderParameter.DESCENDING),
    GOLD(JsonAttributes.Currencies.GOLD, SortingOrderParameter.ASCENDING),
    GOLD_DESC(JsonAttributes.Currencies.GOLD, SortingOrderParameter.DESCENDING),
    DIAMOND(JsonAttributes.Currencies.DIAMOND, SortingOrderParameter.ASCENDING),
    DIAMOND_DESC(JsonAttributes.Currencies.DIAMOND, SortingOrderParameter.DESCENDING),
    EMERALD(JsonAttributes.Currencies.EMERALD, SortingOrderParameter.ASCENDING),
    EMERALD_DESC(JsonAttributes.Currencies.EMERALD, SortingOrderParameter.DESCENDING),
    AMETHYST(JsonAttributes.Currencies.AMETHYST, SortingOrderParameter.ASCENDING),
    AMETHYST_DESC(JsonAttributes.Currencies.AMETHYST, SortingOrderParameter.DESCENDING),
    ID(JsonAttributes.ID, SortingOrderParameter.ASCENDING),
    ID_DESC(JsonAttributes.ID, SortingOrderParameter.DESCENDING),
    NONE(null, null);

    GameSaveOrderBy(String fieldName,
                    SortingOrderParameter order) {
        this.fieldName = fieldName;
        this.order = order;
    }

    private final String fieldName;
    private final SortingOrderParameter order;


    @Override
    public String getFieldName() {
        return this.fieldName;
    }

    @Override
    public SortingOrderParameter getOrder() {
        return this.order;
    }

    public static GameSaveOrderBy fromString(String orderBy) {
        for (GameSaveOrderBy gameSaveOrderBy : GameSaveOrderBy.values()) {
            if (gameSaveOrderBy.name().equalsIgnoreCase(orderBy)) {
                return gameSaveOrderBy;
            }
        }
        throw new IllegalArgumentException("Invalid order by parameter");
    }
}
