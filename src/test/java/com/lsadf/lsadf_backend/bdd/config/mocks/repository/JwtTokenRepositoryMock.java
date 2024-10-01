package com.lsadf.lsadf_backend.bdd.config.mocks.repository;

import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.models.TokenStatus;
import com.lsadf.lsadf_backend.repositories.JwtTokenRepository;
import com.lsadf.lsadf_backend.services.ClockService;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class JwtTokenRepositoryMock extends ARepositoryMock<JwtTokenEntity> implements JwtTokenRepository {

    private final ClockService clockService;


    public JwtTokenRepositoryMock(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public <S extends JwtTokenEntity> S save(S entity) {
        Date now = clockService.nowDate();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        JwtTokenEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            entities.put(entity.getId(), entity);
            return entity;
        }

        toUpdate.setUpdatedAt(now);
        if (toUpdate.getStatus() != entity.getStatus()) {
            toUpdate.setStatus(entity.getStatus());
        }
        if (entity.getExpirationDate() != null) {
            toUpdate.setExpirationDate(entity.getExpirationDate());
        }
        if (entity.getInvalidationDate() != null) {
            toUpdate.setInvalidationDate(entity.getInvalidationDate());
        }

        entities.put(entity.getId(), toUpdate);

        return (S) toUpdate;

    }

    @Override
    public Optional<JwtTokenEntity> findByToken(String token) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getToken().equals(token))
                .findFirst();
    }

    @Override
    public Stream<JwtTokenEntity> findAllByStatusAndExpirationDateAfter(TokenStatus status, Date date) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getStatus().equals(status) && entity.getExpirationDate().after(date));
    }

    @Override
    public Stream<JwtTokenEntity> findAllByExpirationDateBefore(Date date) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getExpirationDate().before(date));
    }
}
