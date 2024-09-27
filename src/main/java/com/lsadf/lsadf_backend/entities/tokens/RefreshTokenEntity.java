package com.lsadf.lsadf_backend.entities.tokens;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import com.lsadf.lsadf_backend.entities.AEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.TokenStatus;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serial;
import java.util.Date;

/**
 * Entity representing a refresh token
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
@Table(name = EntityAttributes.RefreshToken.REFRESH_TOKEN_ENTITY)
@Entity(name = EntityAttributes.RefreshToken.REFRESH_TOKEN_ENTITY)
public class RefreshTokenEntity extends ATokenEntity {
    @Serial
    private static final long serialVersionUID = -5030011223067551882L;
}