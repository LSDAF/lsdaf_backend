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

import java.util.Date;

/**
 * Abstract class for all database entities of Backend
 */
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AEntity {
    private static final String UUID_HIBERNATE_GENERATOR = "uuid-hibernate-generator";
    private static final String UUID_GENERATOR_STRATEGY = "org.hibernate.id.UUIDGenerator";

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
