package com.lsadf.core.entities;

import com.lsadf.core.annotations.StageConsistency;
import com.lsadf.core.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serial;

@Data
@Entity(name = EntityAttributes.Stages.STAGE_ENTITY)
@Table(name = EntityAttributes.Stages.STAGE_ENTITY)
@Builder
@AllArgsConstructor
@ToString(callSuper = true)
@StageConsistency(currentStageField = "currentStage", maxStageField = "maxStage")
public class StageEntity implements com.lsadf.core.entities.Entity {

    @Serial
    private static final long serialVersionUID = -5093458201484300006L;

    @Id
    @Column(name = EntityAttributes.ID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private GameSaveEntity gameSave;

    @Column(name = EntityAttributes.Stages.STAGE_USER_EMAIL)
    private String userEmail;

    @Column(name = EntityAttributes.Stages.STAGE_CURRENT)
    @Positive
    @Builder.Default
    private Long currentStage = 1L;

    @Column(name = EntityAttributes.Stages.STAGE_MAX)
    @Positive
    @Builder.Default
    private Long maxStage = 1L;

    protected StageEntity() {
        super();
    }
}
