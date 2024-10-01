package com.lsadf.lsadf_backend.entities.tokens;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serial;

/**
 * Entity representing a jwt token
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
@Table(name = EntityAttributes.JwtToken.JWT_TOKEN_ENTITY)
@Entity(name = EntityAttributes.JwtToken.JWT_TOKEN_ENTITY)
public class JwtTokenEntity extends ATokenEntity {
    @Serial
    private static final long serialVersionUID = 1142191759177765609L;
}
