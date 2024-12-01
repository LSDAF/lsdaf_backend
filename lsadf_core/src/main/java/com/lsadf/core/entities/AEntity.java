package com.lsadf.core.entities;

import com.lsadf.core.constants.EntityAttributes;
import jakarta.persistence.*;
import java.io.Serial;
import java.util.Date;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public abstract class AEntity implements Entity {

  @Serial private static final long serialVersionUID = 7495963088331648156L;

  @Id
  @Column(name = EntityAttributes.ID)
  @EqualsAndHashCode.Include
  protected String id;

  @Column(name = EntityAttributes.CREATED_AT, nullable = false, updatable = false)
  @CreationTimestamp
  protected Date createdAt;

  @Column(name = EntityAttributes.UPDATED_AT)
  @UpdateTimestamp
  protected Date updatedAt;

  @PrePersist
  public void generateUUID() {
    if (id == null || id.isEmpty()) {
      id = UUID.randomUUID().toString();
    }
  }
}
