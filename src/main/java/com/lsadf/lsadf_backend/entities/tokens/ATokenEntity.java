package com.lsadf.lsadf_backend.entities.tokens;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import com.lsadf.lsadf_backend.entities.AEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.TokenStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@AllArgsConstructor
@Data
public class ATokenEntity extends AEntity {
    protected ATokenEntity() {
        super();
    }

    @Column(name = EntityAttributes.Token.TOKEN)
    protected String token;

    @Column(name = EntityAttributes.Token.EXPIRATION_DATE, nullable = false)
    protected Date expirationDate;

    @Column(name = EntityAttributes.Token.STATUS, nullable = false)
    @Enumerated(EnumType.STRING)
    protected TokenStatus status;

    @Column(name = EntityAttributes.Token.INVALIDATION_DATE)
    protected Date invalidationDate;

    @ManyToOne
    @JoinColumn(name = EntityAttributes.User.USER_ID)
    protected UserEntity user;
}
