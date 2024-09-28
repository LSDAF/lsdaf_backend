package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class AEntity implements Entity {

    @Serial
    private static final long serialVersionUID = 7495963088331648156L;

    protected static final String UUID_HIBERNATE_GENERATOR = "uuid-hibernate-generator";
    protected static final String UUID_GENERATOR_STRATEGY = "org.hibernate.id.UUIDGenerator";

    @Id
    @GeneratedValue(generator = UUID_HIBERNATE_GENERATOR)
    @Column(name = EntityAttributes.ID)
    @GenericGenerator(name = UUID_HIBERNATE_GENERATOR, strategy = UUID_GENERATOR_STRATEGY)
    @EqualsAndHashCode.Include
    protected String id;

    @Column(name = EntityAttributes.CREATED_AT, nullable = false, updatable = false)
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = EntityAttributes.UPDATED_AT)
    @UpdateTimestamp
    protected Date updatedAt;
}
