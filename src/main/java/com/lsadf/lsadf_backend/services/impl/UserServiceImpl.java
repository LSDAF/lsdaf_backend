package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.OAuth2AuthenticationProcessingException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
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

import java.util.HashSet;
import java.util.Map;
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
    private final Cache<LocalUser> localUserCache;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           Cache<LocalUser> localUserCache) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.localUserCache = localUserCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity createUser(UserCreationRequest creationRequest) throws AlreadyExistingUserException {
        if ((creationRequest.getUserId() != null && userRepository.existsById(creationRequest.getUserId()))) {
            throw new AlreadyExistingUserException("User with User id " + creationRequest.getUserId() + " already exist");
        }

        return createUser(creationRequest.getUserId(),
                creationRequest.getEmail(),
                creationRequest.getPassword(),
                creationRequest.getSocialProvider(),
                Sets.newHashSet(UserRole.getDefaultRole()),
                creationRequest.getName(),
                false);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
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
    public UserEntity verifyUser(String userEmail) throws NotFoundException {
        UserEntity userEntity = getUserByEmail(userEmail);
        userEntity.setEnabled(true);
        userEntity.setVerified(true);
        return userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity createUser(String email, String password, SocialProvider provider, Set<UserRole> userRoles, String name, boolean verified) throws AlreadyExistingUserException {
        return createUser(null, email, password, provider, userRoles, name, verified);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity createUser(String id, String email, String password, SocialProvider provider, Set<UserRole> userRoles, String name, boolean verified) throws AlreadyExistingUserException {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistingUserException("User with email " + email + " already exists");
        }
        if (userRoles == null) {
            userRoles = Sets.newHashSet(UserRole.getDefaultRole());
        }

        String encodedPassword = passwordEncoder.encode(password);
        UserEntity userEntity = UserEntity
                .builder()
                .roles(userRoles)
                .email(email)
                .enabled(true)
                .verified(verified)
                .name(name)
                .provider(provider)
                .password(encodedPassword)
                .build();

        if (id != null) {
            userEntity.setId(id);
        }

        return userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserByEmail(String email) throws NotFoundException {
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByEmail(String email) {
        log.info("Checking if user exists by email: {}", email);
        return userRepository.existsByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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

        if (localUserCache.isEnabled()) {
            localUserCache.unset(id);
        }
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
    @Transactional
    @Override
    public UserEntity updateUser(String id, AdminUserUpdateRequest adminUserUpdateRequest) throws NotFoundException, AlreadyExistingUserException {
        UserEntity userEntity = getUserById(id);

        return updateUser(userEntity, adminUserUpdateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) throws NotFoundException, AlreadyExistingUserException {
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

    public UserEntity updateUser(UserEntity userEntity,
                                  AdminUserUpdateRequest adminUserUpdateRequest) throws AlreadyExistingUserException {
        boolean hasUpdates = false;

        if (adminUserUpdateRequest != null) {
            if (adminUserUpdateRequest.getName() != null && !adminUserUpdateRequest.getName().equals(userEntity.getName())) {
                userEntity.setName(adminUserUpdateRequest.getName());
                hasUpdates = true;
            }

            if (adminUserUpdateRequest.getEmail() != null && !adminUserUpdateRequest.getEmail().equals(userEntity.getEmail())) {
                if (existsByEmail(adminUserUpdateRequest.getEmail())) {
                    throw new AlreadyExistingUserException("User with email " + adminUserUpdateRequest.getEmail() + " already exists");
                }
                userEntity.setEmail(adminUserUpdateRequest.getEmail());
                hasUpdates = true;
            }

            if (adminUserUpdateRequest.getPassword() != null && !passwordEncoder.matches(adminUserUpdateRequest.getPassword(), userEntity.getPassword())) {
                    userEntity.setPassword(passwordEncoder.encode(adminUserUpdateRequest.getPassword()));
                    hasUpdates = true;
                }


            if (adminUserUpdateRequest.getUserRoles() != null && !adminUserUpdateRequest.getUserRoles().equals(userEntity.getRoles())) {
                userEntity.setRoles(new HashSet<>(adminUserUpdateRequest.getUserRoles()));
                hasUpdates = true;
            }

            if (adminUserUpdateRequest.getEnabled() != null && !adminUserUpdateRequest.getEnabled().equals(userEntity.getEnabled())) {
                userEntity.setEnabled(adminUserUpdateRequest.getEnabled());
                hasUpdates = true;
            }

            if (adminUserUpdateRequest.getVerified() != null && !adminUserUpdateRequest.getVerified().equals(userEntity.getVerified())) {
                userEntity.setVerified(adminUserUpdateRequest.getVerified());
                hasUpdates = true;
            }
        }


        if (hasUpdates) {
            return userRepository.save(userEntity);
        }

        return userEntity;
    }

    private UserEntity updateUser(UserEntity userEntity,
                                  UserUpdateRequest userUpdateRequest) {
        boolean hasUpdates = false;

        if (userUpdateRequest != null && !userUpdateRequest.getName().equals(userEntity.getName())) {
            userEntity.setName(userUpdateRequest.getName());
            hasUpdates = true;
        }

        if (hasUpdates) {
            return userRepository.save(userEntity);
        }

        return userEntity;
    }
}
