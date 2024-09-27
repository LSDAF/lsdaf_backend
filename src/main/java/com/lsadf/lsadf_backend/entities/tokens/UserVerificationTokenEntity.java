package com.lsadf.lsadf_backend.entities.tokens;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import com.lsadf.lsadf_backend.entities.AEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serial;
import java.util.Date;

/**
 * User Validation Token Entity
 */
@Data
@Entity(name = EntityAttributes.UserVerificationToken.USER_VERIFICATION_TOKEN_ENTITY)
@Table(name = EntityAttributes.UserVerificationToken.USER_VERIFICATION_TOKEN_ENTITY)
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserVerificationTokenEntity extends ATokenEntity {
    @Serial
    private static final long serialVersionUID = -1505183028455730918L;
}
