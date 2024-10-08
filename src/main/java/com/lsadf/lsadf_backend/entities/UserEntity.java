package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.converters.SocialProviderConverter;
import com.lsadf.lsadf_backend.converters.UserRoleConverter;
import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.tokens.UserVerificationTokenEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

/**
 * User Entity
 */
@Data
@Entity(name = EntityAttributes.User.USER_ENTITY)
@Table(name = EntityAttributes.User.USER_ENTITY)
@SuperBuilder
@ToString(callSuper = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AEntity {

    @Serial
    private static final long serialVersionUID = -5927301404833728358L;

    protected UserEntity() {
        super();
    }

    public UserEntity(String id,
                      String name,
                      String email,
                      String password,
                      SocialProvider provider) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    @Column(name = EntityAttributes.User.USER_NAME)
    private String name;

    @Email
    @Column(name = EntityAttributes.User.USER_EMAIL, unique = true)
    private String email;

    @Column(name = EntityAttributes.User.USER_ENABLED)
    private Boolean enabled;

    @Column(name = EntityAttributes.User.USER_PASSWORD)
    @Size(min = 8)
    private String password;

    @Column(name = EntityAttributes.User.USER_VERIFIED)
    private Boolean verified;

    @Convert(converter = SocialProviderConverter.class)
    private SocialProvider provider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<GameSaveEntity> gameSaves = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<RefreshTokenEntity> refreshTokens = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<UserVerificationTokenEntity> validationTokens = new HashSet<>();

    @Column(name = EntityAttributes.User.USER_ROLES)
    @Convert(converter = UserRoleConverter.class)
    @ToString.Exclude
    private Set<UserRole> roles;
}
