package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserUpdateRequest;

import java.util.stream.Stream;

/**
 * Service for managing users
 */
public interface UserService {

    /**
     * @return List of all users
     */
    Stream<User> getUsers();

    /**
     * Search users by name, or by username
     *
     * @param search search query
     * @return Stream of users
     */
    Stream<User> getUsers(String search);

    /**
     * Get user by id
     *
     * @param id
     * @return
     */
    User getUserById(String id);

    /**
     * Get user by email
     *
     * @param email user email
     * @return user
     */
    User getUserByUsername(String email);

    /**
     * Update existing user
     *
     * @param user user to update
     */
    void updateUser(String id, AdminUserUpdateRequest user);

    /**
     * Update existing user
     *
     * @param id   user id
     * @param user user to update
     */
    void updateUser(String id, UserUpdateRequest user);


    /**
     * Reset user password
     * @param id user id
     */
    void resetUserPassword(String id);

    /**
     * Delete user by id
     *
     * @param id user id
     */
    void deleteUser(String id);

    /**
     * Create new user
     *
     * @param request user creation request
     * @return created user
     */
    User createUser(UserCreationRequest request);

    /**
     * Create new user by admin
     *
     * @param adminUserCreationRequest admin user creation request
     * @return created user
     */
    User createUser(AdminUserCreationRequest adminUserCreationRequest);
}
