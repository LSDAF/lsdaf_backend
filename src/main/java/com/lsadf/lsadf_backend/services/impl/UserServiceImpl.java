package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.OAuth2AuthenticationProcessingException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserUpdateRequest;
import com.lsadf.lsadf_backend.security.oauth2.user.OAuth2UserInfo;
import com.lsadf.lsadf_backend.security.oauth2.user.OAuth2UserInfoFactory;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Sets;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Implementation of UserService
 */
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String CHANGEIT = "changeit";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Mapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity createUser(UserCreationRequest creationRequest) {
        if ((creationRequest.getUserId() != null && userRepository.existsById(creationRequest.getUserId()))) {
            throw new AlreadyExistingUserException("User with User id " + creationRequest.getUserId() + " already exist");
        }

        return createUser(creationRequest.getName(),
                creationRequest.getEmail(),
                creationRequest.getPassword(),
                creationRequest.getSocialProvider(),
                Optional.of(Sets.newHashSet(UserRole.getDefaultRole())));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean validateUserPassword(String email, String password) throws NotFoundException {
        UserEntity userEntity = getUserByEmail(email);
        return passwordEncoder.matches(password, userEntity.getPassword());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity createUser(String name, String email, String password, SocialProvider provider, Optional<Set<UserRole>> optionalUserRoles) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistingUserException("User with email " + email + " already exists");
        }

        Set<UserRole> userRoles = optionalUserRoles.orElse(Sets.newHashSet(UserRole.getDefaultRole()));
        String encodedPassword = passwordEncoder.encode(password);
        UserEntity userEntity = UserEntity
                .builder()
                .roles(userRoles)
                .email(email)
                .enabled(true)
                .name(name)
                .provider(provider)
                .password(encodedPassword)
                .build();

        return userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserByEmail(String email) throws NotFoundException {
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        log.info("Checking if user exists by email: {}", email);
        return userRepository.existsByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserById(String id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stream<UserEntity> getUsers() {
        return userRepository.findAllUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteUser(String id) throws NotFoundException {
        if (id == null) {
            throw new NotFoundException("User id is null");
        }
        if (!userRepository.existsById(id)) {
            log.error("User with id {} not found", id);
            throw new NotFoundException("User with id " + id + " not found");
        }

        userRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteUserByEmail(String email) throws NotFoundException {
        if (email == null) {
            throw new NotFoundException("User email is null");
        }
        if (!userRepository.existsByEmail(email)) {
            log.error("User with email {} not found", email);
            throw new NotFoundException("User with email " + email + " not found");
        }
        userRepository.deleteUserEntityByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity updateUserPassword(String userEmail, String oldPassword, String newPassword) throws NotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User with email " + userEmail + " not found"));

        if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity updateUser(String id, UserUpdateRequest userUpdateRequest) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));

        return updateUser(userEntity, userUpdateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) throws NotFoundException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (oAuth2UserInfo.getName() == null) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (oAuth2UserInfo.getEmail() == null) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        UserCreationRequest request = buildUserCreationRequest(registrationId, oAuth2UserInfo);

        UserEntity userEntity = getUserByEmail(oAuth2UserInfo.getEmail());
        if (userEntity != null) {
            if (!userEntity.getProvider().equals(registrationId) && !userEntity.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " + userEntity.getProvider() + " account. Please use your " + userEntity.getProvider() + " account to login.");
            }
            userEntity = updateExistingUser(userEntity, oAuth2UserInfo);
        } else {
            userEntity = createUser(request);
        }


        return LocalUser.create(userEntity, attributes, idToken, userInfo);
    }

    private UserEntity updateExistingUser(UserEntity existingUser, OAuth2UserInfo oAuth2UserInfo) {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(oAuth2UserInfo.getName());
        return updateUser(existingUser, userUpdateRequest);
    }

    private UserCreationRequest buildUserCreationRequest(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return UserCreationRequest.builder()
                .providerUserId(oAuth2UserInfo.getId())
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .socialProvider(SocialProvider.valueOf(registrationId))
                .password(CHANGEIT).build();
    }

    private UserEntity updateUser(UserEntity userEntity,
                                  UserUpdateRequest userUpdateRequest) {
        boolean hasUpdates = false;

        if (userUpdateRequest != null && !userUpdateRequest.getName().equals(userEntity.getName())) {
            userEntity.setName(userEntity.getName());
            hasUpdates = true;
        }

        if (hasUpdates) {
            return userRepository.save(userEntity);
        }

        return userEntity;
    }
}
