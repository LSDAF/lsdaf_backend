package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.utils.UserUtils;
import com.lsadf.lsadf_backend.entities.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of UserService
 */
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User createUser(String name, String email, String password) {
        log.info("Creating user with name: {}, email: {}", name, email);
        UserEntity userEntity = UserEntity
                .builder()
                .email(email)
                .password(password)
                .build();

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return UserUtils.createUserFromEntity(savedUserEntity);
    }

    public User getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);
        return userRepository.findUserEntityByEmail(email)
                .map(UserUtils::createUserFromEntity)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    public User getUserById(String id) {
        log.info("Getting user by id: {}", id);
        return userRepository.findById(id)
                .map(UserUtils::createUserFromEntity)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public void deleteUser(String id) {
        log.info("Deleting user by id: {}", id);
        userRepository.deleteById(id);
    }

    public void deleteUserByEmail(String email) {
        log.info("Deleting user by email: {}", email);
        userRepository.deleteUserEntityByEmail(email);
    }

    public User updateUser(String id, String name, String email) {
        log.info("Updating user with id: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        userEntity.setName(name);
        userEntity.setEmail(email);

        UserEntity updatedUserEntity = userRepository.save(userEntity);

        return UserUtils.createUserFromEntity(updatedUserEntity);
    }

}
