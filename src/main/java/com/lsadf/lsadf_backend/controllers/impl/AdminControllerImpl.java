package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.AdminController;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.AdminService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo() {
        try {
            GlobalInfo globalInfo = adminService.getGlobalInfo();
            return generateResponse(HttpStatus.OK, "Successfully got global info", globalInfo);
        } catch (Exception e) {
            log.error("Error while getting global info: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<User>>> getUsers(@RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        try {
            var users = adminService.getUsers(orderBy);
            int count = users.size();

            return generateResponse(HttpStatus.OK, "Successfully got " + count + " users", users);
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
    public ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserById(@PathVariable(value = USER_ID) String userId) {
        try {
            UserAdminDetails user = adminService.getUserById(userId);
            return generateResponse(HttpStatus.OK, "Successfully got user by id", user);
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
    public ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserByEmail(@PathVariable(value = USER_EMAIL) String userEmail) {
        try {
            UserAdminDetails user = adminService.getUserByEmail(userEmail);
            return generateResponse(HttpStatus.OK, "Successfully got user by id", user);
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
    public ResponseEntity<GenericResponse<User>> updateUser(@PathVariable(value = USER_ID) String userId, @Valid @RequestBody AdminUserUpdateRequest user) {
        try {
            User updatedUser = adminService.updateUser(userId, user);
            return generateResponse(HttpStatus.OK, "Successfully updated user", updatedUser);
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
    public ResponseEntity<GenericResponse<Void>> deleteUser(@PathVariable(value = USER_ID) String userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully deleted user", null);
        } catch (NotFoundException e) {
            log.error("Error while deleting user: ", e);
            return ResponseUtils.generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception while deleting user: ", e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> createUser(@Valid @RequestBody AdminUserCreationRequest adminUserCreationRequest) {
        try {
            User user = adminService.createUser(adminUserCreationRequest);
            return generateResponse(HttpStatus.OK, "Successfully created user", user);
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
    public ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(@RequestParam(value = ORDER_BY, required = false) GameSaveOrderBy orderBy) {
        try {
            List<GameSave> gameSaves = adminService.getGameSaves(orderBy);
            int count = gameSaves.size();
            return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully got " + count + " game saves", gameSaves);
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
    public ResponseEntity<GenericResponse<GameSave>> getGameSave(@PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            GameSave gameSave = adminService.getGameSave(gameSaveId);
            return generateResponse(HttpStatus.OK, "Successfully got game save", gameSave);
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
    public ResponseEntity<GenericResponse<GameSave>> updateGameSave(@PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                    @Valid @RequestBody AdminGameSaveUpdateRequest adminGameSaveUpdateRequest) {
        try {
            GameSave gameSave = adminService.updateGameSave(gameSaveId, adminGameSaveUpdateRequest);
            return generateResponse(HttpStatus.OK, "Successfully updated game save", gameSave);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
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
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@Valid @RequestBody AdminGameSaveCreationRequest creationRequest) {
        try {
            GameSave gameSave = adminService.createGameSave(creationRequest);
            return generateResponse(HttpStatus.OK, "Successfully created game save", gameSave);
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
    public ResponseEntity<GenericResponse<User>> searchUsers(@Valid @RequestBody SearchRequest searchRequest,
                                                             @RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        try {
            var gameSaves = adminService.searchUsers(searchRequest, orderBy);
            int count = gameSaves.size();

            return generateResponse(HttpStatus.OK, "Successfully got " + count + " game saves", gameSaves);
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
    public ResponseEntity<GenericResponse<GameSave>> searchGameSaves(@Valid @RequestBody SearchRequest searchRequest,
                                                                     @RequestParam(value = ORDER_BY) GameSaveOrderBy orderBy) {
        try {
            var gameSaves = adminService.searchGameSaves(searchRequest, orderBy);
            int count = gameSaves.size();

            return generateResponse(HttpStatus.OK, "Successfully got " + count + " game saves", gameSaves);
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
    public ResponseEntity<GenericResponse<Void>> deleteGameSave(@PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            adminService.deleteGameSave(gameSaveId);

            return generateResponse(HttpStatus.OK, "Successfully deleted game save", null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while deleting game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
