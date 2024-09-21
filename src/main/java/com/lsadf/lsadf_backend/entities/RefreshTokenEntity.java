package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Entity representing a refresh token
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = EntityAttributes.RefreshToken.REFRESH_TOKEN_ENTITY)
@Entity(name = EntityAttributes.RefreshToken.REFRESH_TOKEN_ENTITY)
public class RefreshTokenEntity extends AEntity {

    @Column(name = EntityAttributes.RefreshToken.REFRESH_TOKEN_TOKEN, updatable = false, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = EntityAttributes.User.USER_ID)
    private UserEntity user;

    @Column(name = EntityAttributes.RefreshToken.REFRESH_TOKEN_STATUS)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = EntityAttributes.RefreshToken.REFRESH_TOKEN_EXPIRATION_DATE, nullable = false)
    private Date expirationDate;

    @Column(name = EntityAttributes.RefreshToken.REFRESH_TOKEN_INVALIDATION_DATE)
    private Date invalidationDate;

    /**
     * Enum representing the status of the refresh token
     */
    public enum Status {
        ACTIVE,
        INACTIVE
    }
}