package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.OAuth2AuthenticationProcessingException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.requests.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.UserOrderBy;
import com.lsadf.lsadf_backend.security.oauth2.user.OAuth2UserInfo;
import com.lsadf.lsadf_backend.security.oauth2.user.OAuth2UserInfoFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Sets;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
    @Override
    @Transactional
    public UserEntity createUser(String name, String email, String password, SocialProvider provider, Optional<Set<UserRole>> optionalUserRoles) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistingUserException("User with email " + email + " already exists");
        }

        log.info("Creating user with name: {}, email: {}", name, email);

        Set<UserRole> userRoles = optionalUserRoles.orElse(Sets.newHashSet(UserRole.getDefaultRole()));
        String encodedPassword = passwordEncoder.encode(password);
        UserEntity userEntity = UserEntity
                .builder()
                .roles(userRoles)
                .email(email)
                .name(name)
                .provider(provider.getProviderType())
                .password(encodedPassword)
                .build();

        return userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
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
    public UserEntity getUserById(String id) {
        log.info("Getting user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stream<UserEntity> getUsers() {
        log.info("Getting all users");
        return userRepository.findAllUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteUser(String id) {
        log.info("Deleting user by id: {}", id);
        userRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
        log.info("Deleting user by email: {}", email);
        userRepository.deleteUserEntityByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity updateUser(String id, String name, String email) {
        log.info("Updating user with id: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        userEntity.setName(name);
        userEntity.setEmail(email);

        return userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
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
        existingUser.setName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }

    private UserCreationRequest buildUserCreationRequest(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return UserCreationRequest.builder()
                .providerUserId(oAuth2UserInfo.getId())
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .socialProvider(SocialProvider.valueOf(registrationId))
                .password(CHANGEIT).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfo buildUserInfoFromLocalUser(LocalUser localUser) {
        List<UserRole> roles = localUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::fromRole)
                .collect(Collectors.toList());
        UserEntity user = localUser.getUserEntity();
        return new UserInfo(user.getId(), user.getName(), user.getEmail(), roles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfo buildUserInfoFromUserEntity(UserEntity userEntity) {
        List<UserRole> roles = userEntity.getRoles().stream().toList();
        return new UserInfo(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), roles);
    }
}
