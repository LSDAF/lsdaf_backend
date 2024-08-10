package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<User>>> getUsers(UserOrderBy orderBy) {
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
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserById(String userId) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserByEmail(String userEmail) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> updateUser(String userId, User user) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> deleteUser(String userId) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> createUser(UserCreationRequest userCreationRequest) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(GameSaveOrderBy orderBy, String userId) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> getGameSave(String gameSaveId) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> updateGameSave(String gameSaveId, GameSave gameSave) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(AdminGameSaveCreationRequest creationRequest) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> searchUsers(SearchRequest searchRequest, UserOrderBy orderBy) {
        try {
            var gameSaves = adminService.searchUsers(searchRequest, orderBy);
            int count = gameSaves.size();

            return generateResponse(HttpStatus.OK, "Successfully got " + count + " game saves", gameSaves);
        } catch (Exception e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> searchGameSaves(SearchRequest searchRequest, GameSaveOrderBy orderBy) {
        try {
            var gameSaves = adminService.searchGameSaves(searchRequest, orderBy);
            int count = gameSaves.size();

            return generateResponse(HttpStatus.OK, "Successfully got " + count + " game saves", gameSaves);
        } catch (Exception e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
