package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.WrongPasswordException;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserUpdateRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Service for managing users
 */
public interface UserService {
    /**
     * Creates a new user
     *
     * @param id                the id of the user if any
     * @param email             the email of the user
     * @param password          the password of the user
     * @param provider          the social provider if any, else local
     * @param optionalUserRoles the roles of the user if any to use
     * @param name              the name of the user
     * @param verified
     * @return the created user
     */
    UserEntity createUser(String id, String email, String password, SocialProvider provider, Set<UserRole> optionalUserRoles, String name, boolean verified) throws AlreadyExistingUserException;

    /**
     * Validates a user
     *
     * @param userEmail the email of the user
     * @return the validated user
     */
    UserEntity verifyUser(String userEmail) throws NotFoundException;

    /**
     * Creates a new user
     *
     * @param userRoles the roles of the user if any to use
     * @param email     the email of the user
     * @param password  the password of the user
     * @param provider  the social provider if any, else local
     * @param name      the name of the user
     * @param verified  the verification status of the user
     * @return the created user
     */
    UserEntity createUser(String email, String password, SocialProvider provider, Set<UserRole> userRoles, String name, boolean verified) throws AlreadyExistingUserException;


    /**
     * Creates a new user
     *
     * @param creationRequest the user creation request
     * @return the created user
     */
    UserEntity createUser(UserCreationRequest creationRequest) throws AlreadyExistingUserException;

    /**
     * Validates given user password
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @throws WrongPasswordException if the password is wrong
     */
    boolean validateUserPassword(String email, String password) throws NotFoundException;

    /**
     * Gets all users
     *
     * @return the list of users
     */
    Stream<UserEntity> getUsers();

    /**
     * Gets user by email
     *
     * @param email
     * @return user
     */
    UserEntity getUserByEmail(String email) throws NotFoundException;

    /**
     * Checks if user exists by email
     *
     * @param email the email of the user
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Gets user by id
     *
     * @param id
     * @return
     */
    UserEntity getUserById(String id) throws NotFoundException;

    /**
     * Updates user by id
     *
     * @param id                the id of the user
     * @param userUpdateRequest the update request
     * @return
     */
    UserEntity updateUser(String id, UserUpdateRequest userUpdateRequest) throws NotFoundException;

    /**
     * Updates user by id
     *
     * @param id                     the id of the user
     * @param adminUserUpdateRequest the request to update the user data
     * @return the updated user
     * @throws NotFoundException if the user is not found
     */
    UserEntity updateUser(String id, AdminUserUpdateRequest adminUserUpdateRequest) throws NotFoundException, AlreadyExistingUserException;

    /**
     * Updates user password with its user email
     *
     * @param userEmail   the user email
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return
     */
    UserEntity updateUserPassword(String userEmail, String oldPassword, String newPassword) throws NotFoundException;

    /**
     * Deletes user by id
     *
     * @param id
     */
    void deleteUser(String id) throws NotFoundException;

    /**
     * Deletes user by email
     *
     * @param email
     */
    void deleteUserByEmail(String email) throws NotFoundException;

    /**
     * Processes user registration
     *
     * @param registrationId
     * @param attributes
     * @param idToken
     * @param userInfo
     * @return
     */
    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) throws NotFoundException, AlreadyExistingUserException;
}
