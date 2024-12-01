package com.lsadf.core.entities;

import com.lsadf.core.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity(name = EntityAttributes.Characteristics.CHARACTERISTICS_ENTITY)
@Table(name = EntityAttributes.Characteristics.CHARACTERISTICS_ENTITY)
@SuperBuilder
@AllArgsConstructor
@ToString(callSuper = true)
public class CharacteristicsEntity implements com.lsadf.core.entities.Entity {

  @Serial private static final long serialVersionUID = 2100417080983225675L;

  protected CharacteristicsEntity() {
    super();
  }

  @Id
  @Column(name = EntityAttributes.ID)
  private String id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private GameSaveEntity gameSave;

  @Column(name = EntityAttributes.Characteristics.CHARACTERISTICS_ATTACK)
  @Positive
  @Builder.Default
  private Long attack = 1L;

  @Column(name = EntityAttributes.Characteristics.CHARACTERISTICS_CRIT_CHANCE)
  @Positive
  @Builder.Default
  private Long critChance = 1L;

  @Column(name = EntityAttributes.Characteristics.CHARACTERISTICS_CRIT_DAMAGE)
  @Positive
  @Builder.Default
  private Long critDamage = 1L;

  @Column(name = EntityAttributes.Characteristics.CHARACTERISTICS_HEALTH)
  @Positive
  @Builder.Default
  private Long health = 1L;

  @Column(name = EntityAttributes.Characteristics.CHARACTERISTICS_RESISTANCE)
  @Positive
  @Builder.Default
  private Long resistance = 1L;
}
