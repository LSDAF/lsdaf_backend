package com.lsadf.controllers.impl;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;
import static com.lsadf.core.utils.TokenUtils.getUsernameFromJwt;

import com.lsadf.controllers.CharacteristicsController;
import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.Characteristics;
import com.lsadf.core.requests.characteristics.CharacteristicsRequest;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.services.CacheService;
import com.lsadf.core.services.CharacteristicsService;
import com.lsadf.core.services.GameSaveService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CharacteristicsControllerImpl extends BaseController
    implements CharacteristicsController {
  private final GameSaveService gameSaveService;
  private final CharacteristicsService characteristicsService;
  private final CacheService cacheService;

  private final Mapper mapper;

  @Autowired
  public CharacteristicsControllerImpl(
      GameSaveService gameSaveService,
      CharacteristicsService characteristicsService,
      CacheService cacheService,
      Mapper mapper) {
    this.gameSaveService = gameSaveService;
    this.characteristicsService = characteristicsService;
    this.cacheService = cacheService;
    this.mapper = mapper;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> saveCharacteristics(
      Jwt jwt, String gameSaveId, CharacteristicsRequest characteristicsRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

    Characteristics characteristics =
        mapper.mapCharacteristicsRequestToCharacteristics(characteristicsRequest);
    characteristicsService.saveCharacteristics(
        gameSaveId, characteristics, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> getCharacteristics(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Characteristics characteristics = characteristicsService.getCharacteristics(gameSaveId);
    return generateResponse(HttpStatus.OK, characteristics);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}