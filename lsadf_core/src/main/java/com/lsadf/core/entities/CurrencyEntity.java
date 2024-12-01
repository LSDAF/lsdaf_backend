package com.lsadf.core.entities;

import com.lsadf.core.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity(name = EntityAttributes.Currencies.CURRENCY_ENTITY)
@Table(name = EntityAttributes.Currencies.CURRENCY_ENTITY)
@SuperBuilder
@AllArgsConstructor
@ToString(callSuper = true)
public class CurrencyEntity implements com.lsadf.core.entities.Entity {

  @Serial private static final long serialVersionUID = 1142981637978170899L;

  protected CurrencyEntity() {
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

  @Column(name = EntityAttributes.Currencies.CURRENCY_USER_EMAIL)
  private String userEmail;

  @Column(name = EntityAttributes.Currencies.CURRENCY_GOLD_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long goldAmount = 0L;

  @Column(name = EntityAttributes.Currencies.CURRENCY_DIAMOND_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long diamondAmount = 0L;

  @Column(name = EntityAttributes.Currencies.CURRENCY_EMERALD_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long emeraldAmount = 0L;

  @Column(name = EntityAttributes.Currencies.CURRENCY_AMETHYST_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long amethystAmount = 0L;
}
