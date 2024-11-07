package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.CharacteristicsController;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Characteristics;
import com.lsadf.lsadf_backend.requests.characteristics.CharacteristicsRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.CharacteristicsService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.REDIS_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;
import static com.lsadf.lsadf_backend.utils.TokenUtils.getUsernameFromJwt;

@RestController
@Slf4j
public class CharacteristicsControllerImpl extends BaseController implements CharacteristicsController {
    private final GameSaveService gameSaveService;
    private final CharacteristicsService characteristicsService;
    private final CacheService cacheService;

    private final Mapper mapper;

    @Autowired
    public CharacteristicsControllerImpl(GameSaveService gameSaveService,
                                         CharacteristicsService characteristicsService,
                                         @Qualifier(REDIS_CACHE_SERVICE) CacheService cacheService,
                                         Mapper mapper) {
        this.gameSaveService = gameSaveService;
        this.characteristicsService = characteristicsService;
        this.cacheService = cacheService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveCharacteristics(Jwt jwt,
                                                                     String gameSaveId,
                                                                     CharacteristicsRequest characteristicsRequest) {
        validateUser(jwt);
        String userEmail = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

        Characteristics characteristics = mapper.mapCharacteristicsRequestToCharacteristics(characteristicsRequest);
        characteristicsService.saveCharacteristics(gameSaveId, characteristics, cacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> getCharacteristics(Jwt jwt,
                                                                    String gameSaveId) {
        validateUser(jwt);
        String userEmail = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
        Characteristics characteristics = characteristicsService.getCharacteristics(gameSaveId);
        return generateResponse(HttpStatus.OK, characteristics);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }
}
