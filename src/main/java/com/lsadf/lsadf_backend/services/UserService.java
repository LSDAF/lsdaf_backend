package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.requests.UserCreationRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Service for managing users
 */
public interface UserService {
    /**
     * Creates a new user
     *
     * @param name              the name of the user
     * @param email             the email of the user
     * @param password          the password of the user
     * @param provider          the social provider if any, else local
     * @param optionalUserRoles the roles of the user if any to use
     * @return the created user
     */
    UserEntity createUser(String name, String email, String password, SocialProvider provider, Optional<Set<UserRole>> optionalUserRoles);

    /**
     * Creates a new user
     *
     * @param creationRequest the user creation request
     * @return the created user
     */
    UserEntity createUser(UserCreationRequest creationRequest);

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
    UserEntity getUserByEmail(String email);

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
    UserEntity getUserById(String id);

    /**
     * Updates user by id
     *
     * @param id
     * @param name
     * @param email
     * @return
     */
    UserEntity updateUser(String id, String name, String email);

    /**
     * Deletes user by id
     *
     * @param id
     */
    void deleteUser(String id);

    /**
     * Deletes user by email
     *
     * @param email
     */
    void deleteUserByEmail(String email);

    /**
     * Build user info from LocalUser
     *
     * @param localUser the local user
     * @return user info
     */
    UserInfo buildUserInfoFromLocalUser(LocalUser localUser);

    /**
     * Build user info from UserEntity
     *
     * @param userEntity the user entity
     * @return user info
     */
    UserInfo buildUserInfoFromUserEntity(UserEntity userEntity);

    /**
     * Processes user registration
     *
     * @param registrationId
     * @param attributes
     * @param idToken
     * @param userInfo
     * @return
     */
    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}