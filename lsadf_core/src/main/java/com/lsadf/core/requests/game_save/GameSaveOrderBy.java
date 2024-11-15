package com.lsadf.core.requests.game_save;

import com.lsadf.core.constants.JsonAttributes;
import com.lsadf.core.constants.SortingOrderParameter;
import com.lsadf.core.requests.common.OrderBy;

public enum GameSaveOrderBy implements OrderBy {
    CREATED_AT(JsonAttributes.CREATED_AT, SortingOrderParameter.ASCENDING),
    CREATED_AT_DESC(JsonAttributes.CREATED_AT, SortingOrderParameter.DESCENDING),
    UPDATED_AT(JsonAttributes.UPDATED_AT, SortingOrderParameter.ASCENDING),
    UPDATED_AT_DESC(JsonAttributes.UPDATED_AT, SortingOrderParameter.DESCENDING),
    GOLD(JsonAttributes.Currency.GOLD, SortingOrderParameter.ASCENDING),
    GOLD_DESC(JsonAttributes.Currency.GOLD, SortingOrderParameter.DESCENDING),
    DIAMOND(JsonAttributes.Currency.DIAMOND, SortingOrderParameter.ASCENDING),
    DIAMOND_DESC(JsonAttributes.Currency.DIAMOND, SortingOrderParameter.DESCENDING),
    EMERALD(JsonAttributes.Currency.EMERALD, SortingOrderParameter.ASCENDING),
    EMERALD_DESC(JsonAttributes.Currency.EMERALD, SortingOrderParameter.DESCENDING),
    AMETHYST(JsonAttributes.Currency.AMETHYST, SortingOrderParameter.ASCENDING),
    AMETHYST_DESC(JsonAttributes.Currency.AMETHYST, SortingOrderParameter.DESCENDING),
    NICKNAME(JsonAttributes.GameSave.NICKNAME, SortingOrderParameter.ASCENDING),
    NICKNAME_DESC(JsonAttributes.GameSave.NICKNAME, SortingOrderParameter.DESCENDING),
    MAX_STAGE(JsonAttributes.Stage.MAX_STAGE, SortingOrderParameter.ASCENDING),
    MAX_STAGE_DESC(JsonAttributes.Stage.MAX_STAGE, SortingOrderParameter.DESCENDING),
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
