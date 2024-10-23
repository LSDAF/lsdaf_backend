package com.lsadf.lsadf_backend.mappers.impl;

import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
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
        Currency currency = mapCurrencyEntityToCurrency(gameSaveEntity.getCurrencyEntity());

        return GameSave.builder()
                .id(gameSaveEntity.getId())
                .userEmail(gameSaveEntity.getUserEmail())
                .nickname(gameSaveEntity.getNickname())
                .currency(currency)
                .stage(stage)
                .healthPoints(gameSaveEntity.getHealthPoints())
                .attack(gameSaveEntity.getAttack())
                .id(gameSaveEntity.getId())
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
                .id(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
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
                .enabled(adminUserCreationRequest.getEnabled())
                .build();
    }
}
