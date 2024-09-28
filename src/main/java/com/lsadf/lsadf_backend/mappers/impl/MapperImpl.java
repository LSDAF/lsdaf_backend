package com.lsadf.lsadf_backend.mappers.impl;

import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lsadf.lsadf_backend.models.LocalUser.buildSimpleGrantedAuthorities;

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
    public GameSave mapToGameSave(GameSaveEntity gameSaveEntity) {
        return GameSave.builder()
                .id(gameSaveEntity.getId())
                .userEmail(gameSaveEntity.getUser().getEmail())
                .userId(gameSaveEntity.getUser().getId())
                .nickname(gameSaveEntity.getNickname())
                .gold(gameSaveEntity.getCurrencyEntity().getGoldAmount())
                .diamond(gameSaveEntity.getCurrencyEntity().getDiamondAmount())
                .emerald(gameSaveEntity.getCurrencyEntity().getEmeraldAmount())
                .amethyst(gameSaveEntity.getCurrencyEntity().getAmethystAmount())
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
    public User mapToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .socialProvider(userEntity.getProvider())
                .userRoles(userEntity.getRoles().stream().toList())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .email(userEntity.getEmail())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAdminDetails mapToUserAdminDetails(UserEntity userEntity) {
        SocialProvider provider = userEntity.getProvider() == null ? null : userEntity.getProvider();
        return UserAdminDetails.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .socialProvider(provider)
                .enabled(userEntity.isEnabled())
                .userRoles(new ArrayList<>(userEntity.getRoles()))
                .gameSaves(userEntity.getGameSaves().stream()
                        .map(this::mapToGameSave)
                        .collect(Collectors.toList()))
                .updatedAt(userEntity.getUpdatedAt())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfo mapLocalUserToUserInfo(LocalUser localUser) {
        List<UserRole> roles = localUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::fromRole)
                .collect(Collectors.toList());
        UserEntity user = localUser.getUserEntity();
        return new UserInfo(user.getId(), user.getName(), user.getEmail(), roles, user.getCreatedAt(), user.getUpdatedAt());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfo mapUserEntityToUserInfo(UserEntity userEntity) {
        List<UserRole> roles = userEntity.getRoles().stream().toList();
        return new UserInfo(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), roles, userEntity.getCreatedAt(), userEntity.getUpdatedAt());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalUser mapUserEntityToLocalUser(UserEntity user) {
        return new LocalUser(user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                buildSimpleGrantedAuthorities(user.getRoles()),
                user);
    }
}
