package com.lsadf.lsadf_backend.controllers.admin.impl;

import com.lsadf.lsadf_backend.controllers.admin.AdminGlobalInfoController;
import com.lsadf.lsadf_backend.controllers.impl.BaseController;
import com.lsadf.lsadf_backend.models.GlobalInfo;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

/**
 * The implementation of the AdminGlobalInfoController
 */
@RestController
@Slf4j
public class AdminGlobalInfoControllerImpl extends BaseController implements AdminGlobalInfoController {

    private final UserService userService;
    private final GameSaveService gameSaveService;
    private final ClockService clockService;

    @Autowired
    public AdminGlobalInfoControllerImpl(UserService userService,
                                         GameSaveService gameSaveService,
                                         ClockService clockService) {
        this.userService = userService;
        this.gameSaveService = gameSaveService;
        this.clockService = clockService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo(Jwt jwt) {
        validateUser(jwt);
        Long userCount = userService.getUsers().count();
        Long gameSaveCount = gameSaveService.getGameSaves().count();
        Instant now = clockService.nowInstant();

        GlobalInfo globalInfo = GlobalInfo.builder()
                .userCounter(userCount)
                .now(now)
                .gameSaveCounter(gameSaveCount)
                .build();
        return generateResponse(HttpStatus.OK, globalInfo);
    }
}
