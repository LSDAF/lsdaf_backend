package com.lsadf.lsadf_backend.mappers.impl;

import com.lsadf.lsadf_backend.entities.CharacteristicsEntity;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.characteristics.CharacteristicsRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Date;

@NoArgsConstructor
public class MapperImpl implements Mapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public Characteristics mapCharacteristicsRequestToCharacteristics(CharacteristicsRequest characteristicsRequest) {
        return new Characteristics(characteristicsRequest.getAttack(),
                characteristicsRequest.getCritChance(),
                characteristicsRequest.getCritDamage(),
                characteristicsRequest.getHealth(),
                characteristicsRequest.getResistance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Characteristics mapCharacteristicsEntityToCharacteristics(CharacteristicsEntity characteristicsEntity) {
        return new Characteristics(characteristicsEntity.getAttack(),
                characteristicsEntity.getCritChance(),
                characteristicsEntity.getCritDamage(),
                characteristicsEntity.getHealth(),
                characteristicsEntity.getResistance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency mapCurrencyRequestToCurrency(CurrencyRequest currencyRequest) {
        return new Currency(currencyRequest.getGold(),
                currencyRequest.getDiamond(),
                currencyRequest.getEmerald(),
                currencyRequest.getAmethyst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSave mapGameSaveEntityToGameSave(GameSaveEntity gameSaveEntity) {
        Stage stage = mapStageEntityToStage(gameSaveEntity.getStageEntity());
        Characteristics characteristics = mapCharacteristicsEntityToCharacteristics(gameSaveEntity.getCharacteristicsEntity());
        Currency currency = mapCurrencyEntityToCurrency(gameSaveEntity.getCurrencyEntity());

        return GameSave.builder()
                .id(gameSaveEntity.getId())
                .userEmail(gameSaveEntity.getUserEmail())
                .nickname(gameSaveEntity.getNickname())
                .characteristics(characteristics)
                .currency(currency)
                .stage(stage)
                .createdAt(gameSaveEntity.getCreatedAt())
                .updatedAt(gameSaveEntity.getUpdatedAt())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency mapCurrencyEntityToCurrency(CurrencyEntity currencyEntity) {
        return new Currency(currencyEntity.getGoldAmount(),
                currencyEntity.getDiamondAmount(),
                currencyEntity.getEmeraldAmount(),
                currencyEntity.getAmethystAmount());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stage mapStageEntityToStage(StageEntity stageEntity) {
        return Stage.builder()
                .maxStage(stageEntity.getMaxStage())
                .currentStage(stageEntity.getCurrentStage())
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Stage mapStageRequestToStage(StageRequest stageRequest) {
        return Stage.builder()
                .maxStage(stageRequest.getMaxStage())
                .currentStage(stageRequest.getCurrentStage())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User mapUserRepresentationToUser(UserRepresentation userRepresentation) {
        Date createdTimestamp = (userRepresentation.getCreatedTimestamp() != null) ? new Date(userRepresentation.getCreatedTimestamp()) : null;
        return User.builder()
                .username(userRepresentation.getUsername())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .id(userRepresentation.getId())
                .emailVerified(userRepresentation.isEmailVerified())
                .enabled(userRepresentation.isEnabled())
                .createdTimestamp(createdTimestamp)
                .userRoles(userRepresentation.getRealmRoles())
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserRepresentation mapUserCreationRequestToUserRepresentation(UserCreationRequest userCreationRequest) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userCreationRequest.getUsername());
        userRepresentation.setFirstName(userCreationRequest.getFirstName());
        userRepresentation.setLastName(userCreationRequest.getLastName());
        userRepresentation.setEmailVerified(userCreationRequest.isEmailVerified());
        userRepresentation.setEnabled(userCreationRequest.isEnabled());
        return userRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserCreationRequest mapAdminUserCreationRequestToUserCreationRequest(AdminUserCreationRequest adminUserCreationRequest) {
        return UserCreationRequest.builder()
                .username(adminUserCreationRequest.getUsername())
                .firstName(adminUserCreationRequest.getFirstName())
                .lastName(adminUserCreationRequest.getLastName())
                .emailVerified(adminUserCreationRequest.getEmailVerified())
                .userRoles(adminUserCreationRequest.getUserRoles())
                .enabled(adminUserCreationRequest.getEnabled())
                .build();
    }
}