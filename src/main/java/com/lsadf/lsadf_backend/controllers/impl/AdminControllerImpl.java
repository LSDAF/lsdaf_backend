package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.controllers.AdminController;
import com.lsadf.lsadf_backend.exceptions.*;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.AdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;
import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

/**
 * Implementation of AdminController
 */
@Slf4j
@RestController
public class AdminControllerImpl extends BaseController implements AdminController {
    private final AdminService adminService;

    public AdminControllerImpl(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            GlobalInfo globalInfo = adminService.getGlobalInfo();
            return generateResponse(HttpStatus.OK, globalInfo);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting global info: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting global info: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            boolean cacheEnabled = adminService.isRedisCacheEnabled();
            return generateResponse(HttpStatus.OK, cacheEnabled);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while checking cache status: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while checking cache status: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Boolean>> toggleRedisCacheEnabling(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            adminService.toggleCache();
            Boolean cacheEnabled = adminService.isRedisCacheEnabled();
            return generateResponse(HttpStatus.OK, cacheEnabled);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while toggling cache: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while toggling cache: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<User>>> getUsers(@CurrentUser LocalUser localUser,
                                                                @RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        try {
            validateUser(localUser);
            List<User> users = adminService.getUsers(orderBy);

            return generateResponse(HttpStatus.OK, users);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting users: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserById(@CurrentUser LocalUser localUser,
                                                                                 @PathVariable(value = USER_ID) String userId) {
        try {
            validateUser(localUser);
            UserAdminDetails user = adminService.getUserById(userId);
            return generateResponse(HttpStatus.OK, user);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting user by id: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Error while getting user by id: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting user by id: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserByEmail(@CurrentUser LocalUser localUser,
                                                                                    @PathVariable(value = USER_EMAIL) String userEmail) {
        try {
            validateUser(localUser);
            UserAdminDetails user = adminService.getUserByEmail(userEmail);
            return generateResponse(HttpStatus.OK, user);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting user by id: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Error while getting user by id: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting user by id: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> updateUser(@CurrentUser LocalUser localUser,
                                                            @PathVariable(value = USER_ID) String userId,
                                                            @Valid @RequestBody AdminUserUpdateRequest user) {
        try {
            validateUser(localUser);
            User updatedUser = adminService.updateUser(userId, user);
            return generateResponse(HttpStatus.OK, updatedUser);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while updating user: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Error while updating user: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while updating user: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> deleteUser(@CurrentUser LocalUser localUser,
                                                            @PathVariable(value = USER_ID) String userId) {
        try {
            validateUser(localUser);
            adminService.deleteUser(userId);
            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while deleting user: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Error while deleting user: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception while deleting user: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> createUser(@CurrentUser LocalUser localUser,
                                                            @Valid @RequestBody AdminUserCreationRequest adminUserCreationRequest) {
        try {
            validateUser(localUser);
            User user = adminService.createUser(adminUserCreationRequest);
            return generateResponse(HttpStatus.OK, user);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while creating user: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (AlreadyExistingUserException e) {
            log.error("Error while creating user: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while creating user: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(@CurrentUser LocalUser localUser,
                                                                        @RequestParam(value = ORDER_BY, required = false) GameSaveOrderBy orderBy) {
        try {
            validateUser(localUser);
            List<GameSave> gameSaves = adminService.getGameSaves(orderBy);
            return generateResponse(HttpStatus.OK, gameSaves);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting game saves: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting game saves: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> getGameSave(@CurrentUser LocalUser localUser,
                                                                 @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(localUser);
            GameSave gameSave = adminService.getGameSave(gameSaveId);
            return generateResponse(HttpStatus.OK, gameSave);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting game save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> updateGameSave(@CurrentUser LocalUser localUser,
                                                                    @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                    @Valid @RequestBody AdminGameSaveUpdateRequest adminGameSaveUpdateRequest) {
        try {
            validateUser(localUser);
            GameSave gameSave = adminService.updateGameSave(gameSaveId, adminGameSaveUpdateRequest);
            return generateResponse(HttpStatus.OK, gameSave);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while updating game save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (AlreadyTakenNicknameException e) {
            log.error("AlreadyTakenNicknameException exception while saving game: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, "The given nickname is already taken.", null);
        } catch (Exception e) {
            log.error("Error while updating game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@CurrentUser LocalUser localUser,
                                                                         @Valid @RequestBody AdminGameSaveCreationRequest creationRequest) {
        try {
            validateUser(localUser);
            GameSave gameSave = adminService.createGameSave(creationRequest);
            return generateResponse(HttpStatus.OK, gameSave);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while creating game save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (AlreadyExistingGameSaveException e) {
            log.error("Game Save with id {} already exists", creationRequest.getId());
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while creating game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> searchUsers(@CurrentUser LocalUser localUser,
                                                             @Valid @RequestBody SearchRequest searchRequest,
                                                             @RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        try {
            validateUser(localUser);
            List<User> gameSaves = adminService.searchUsers(searchRequest, orderBy);
            int count = gameSaves.size();

            return generateResponse(HttpStatus.OK, gameSaves);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while searching users: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException exception while searching users: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while searching users: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> searchGameSaves(@CurrentUser LocalUser localUser,
                                                                     @Valid @RequestBody SearchRequest searchRequest,
                                                                     @RequestParam(value = ORDER_BY) GameSaveOrderBy orderBy) {
        try {
            validateUser(localUser);
            List<GameSave> gameSaves = adminService.searchGameSaves(searchRequest, orderBy);
            int count = gameSaves.size();

            return generateResponse(HttpStatus.OK, gameSaves);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while searching game saves: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException exception while searching users: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while searching game saves: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> deleteGameSave(@CurrentUser LocalUser localUser,
                                                                @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(localUser);

            adminService.deleteGameSave(gameSaveId);
            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while deleting game save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while deleting game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> flushAndClearCache(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);

            adminService.flushAndClearCache();

            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while clearing cache: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while clearing cache: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
