package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * User Entity
 */
@Data
@Entity(name = EntityAttributes.User.USER_ENTITY)
@Table(name = EntityAttributes.User.USER_ENTITY)
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AEntity {
    protected UserEntity() {
        super();
    }

    public UserEntity(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Column(name = EntityAttributes.User.USER_NAME)
    private String name;

    @Email
    @Column(name = EntityAttributes.User.USER_EMAIL, unique = true)
    private String email;

    @Column(name = EntityAttributes.User.USER_PASSWORD)
    @Size(min = 8)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<GameSaveEntity> gameSaves = new HashSet<>();
}
