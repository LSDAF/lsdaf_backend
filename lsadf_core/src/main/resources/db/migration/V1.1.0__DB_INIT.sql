CREATE TABLE t_characteristics
(
    game_save_id VARCHAR(255) NOT NULL,
    attack       BIGINT,
    crit_chance  BIGINT,
    crit_damage  BIGINT,
    health       BIGINT,
    resistance   BIGINT,
    CONSTRAINT pk_t_characteristics PRIMARY KEY (game_save_id)
);

CREATE TABLE t_currency
(
    game_save_id    VARCHAR(255) NOT NULL,
    user_email      VARCHAR(255),
    gold_amount     BIGINT,
    diamond_amount  BIGINT,
    emerald_amount  BIGINT,
    amethyst_amount BIGINT,
    CONSTRAINT pk_t_currency PRIMARY KEY (game_save_id)
);

CREATE TABLE t_game_save
(
    id                 VARCHAR(255) NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITHOUT TIME ZONE,
    user_email         VARCHAR(255),
    nickname           VARCHAR(255),
    characteristics_id VARCHAR(255),
    currency_id        VARCHAR(255),
    inventory_id       VARCHAR(255),
    stage_id           VARCHAR(255),
    CONSTRAINT pk_t_game_save PRIMARY KEY (id)
);

CREATE TABLE t_inventory
(
    game_save_id VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_t_inventory PRIMARY KEY (game_save_id)
);

CREATE TABLE t_inventory_items
(
    items_id                 VARCHAR(255) NOT NULL,
    t_inventory_game_save_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_t_inventory_items PRIMARY KEY (items_id, t_inventory_game_save_id)
);

CREATE TABLE t_item
(
    id                            VARCHAR(255) NOT NULL,
    created_at                    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at                    TIMESTAMP WITHOUT TIME ZONE,
    inventory_entity_game_save_id VARCHAR(255),
    client_id                     VARCHAR(255),
    blueprint_id                  VARCHAR(255),
    type                          VARCHAR(255),
    rarity                        VARCHAR(255),
    is_equipped                   BOOLEAN,
    level                         INTEGER,
    statistic                     VARCHAR(255),
    base_value                    FLOAT,
    CONSTRAINT pk_t_item PRIMARY KEY (id)
);

CREATE TABLE t_stage
(
    game_save_id  VARCHAR(255) NOT NULL,
    user_email    VARCHAR(255),
    current_stage BIGINT,
    max_stage     BIGINT,
    CONSTRAINT pk_t_stage PRIMARY KEY (game_save_id)
);

ALTER TABLE t_game_save
    ADD CONSTRAINT uc_t_game_save_characteristics UNIQUE (characteristics_id);

ALTER TABLE t_game_save
    ADD CONSTRAINT uc_t_game_save_currency UNIQUE (currency_id);

ALTER TABLE t_game_save
    ADD CONSTRAINT uc_t_game_save_inventory UNIQUE (inventory_id);

ALTER TABLE t_game_save
    ADD CONSTRAINT uc_t_game_save_nickname UNIQUE (nickname);

ALTER TABLE t_game_save
    ADD CONSTRAINT uc_t_game_save_stage UNIQUE (stage_id);

ALTER TABLE t_inventory_items
    ADD CONSTRAINT uc_t_inventory_items_items UNIQUE (items_id);

ALTER TABLE t_item
    ADD CONSTRAINT uc_t_item_client UNIQUE (client_id);

ALTER TABLE t_characteristics
    ADD CONSTRAINT FK_T_CHARACTERISTICS_ON_GAMESAVE FOREIGN KEY (game_save_id) REFERENCES t_game_save (id);

ALTER TABLE t_currency
    ADD CONSTRAINT FK_T_CURRENCY_ON_GAMESAVE FOREIGN KEY (game_save_id) REFERENCES t_game_save (id);

ALTER TABLE t_game_save
    ADD CONSTRAINT FK_T_GAME_SAVE_ON_CHARACTERISTICS FOREIGN KEY (characteristics_id) REFERENCES t_characteristics (game_save_id);

ALTER TABLE t_game_save
    ADD CONSTRAINT FK_T_GAME_SAVE_ON_CURRENCY FOREIGN KEY (currency_id) REFERENCES t_currency (game_save_id);

ALTER TABLE t_game_save
    ADD CONSTRAINT FK_T_GAME_SAVE_ON_INVENTORY FOREIGN KEY (inventory_id) REFERENCES t_inventory (game_save_id);

ALTER TABLE t_game_save
    ADD CONSTRAINT FK_T_GAME_SAVE_ON_STAGE FOREIGN KEY (stage_id) REFERENCES t_stage (game_save_id);

ALTER TABLE t_inventory
    ADD CONSTRAINT FK_T_INVENTORY_ON_GAMESAVE FOREIGN KEY (game_save_id) REFERENCES t_game_save (id);

ALTER TABLE t_item
    ADD CONSTRAINT FK_T_ITEM_ON_INVENTORYENTITY_GAMESAVE FOREIGN KEY (inventory_entity_game_save_id) REFERENCES t_inventory (game_save_id);

ALTER TABLE t_stage
    ADD CONSTRAINT FK_T_STAGE_ON_GAMESAVE FOREIGN KEY (game_save_id) REFERENCES t_game_save (id);

ALTER TABLE t_inventory_items
    ADD CONSTRAINT fk_tinvite_on_inventory_entity FOREIGN KEY (t_inventory_game_save_id) REFERENCES t_inventory (game_save_id);

ALTER TABLE t_inventory_items
    ADD CONSTRAINT fk_tinvite_on_item_entity FOREIGN KEY (items_id) REFERENCES t_item (id);