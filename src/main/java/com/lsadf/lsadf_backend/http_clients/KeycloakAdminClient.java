package com.lsadf.lsadf_backend.http_clients;

import com.lsadf.lsadf_backend.configurations.http_clients.KeycloakAdminFeignConfiguration;
import com.lsadf.lsadf_backend.constants.HttpClientTypes;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import com.lsadf.lsadf_backend.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = HttpClientTypes.KEYCLOAK_ADMIN, configuration = KeycloakAdminFeignConfiguration.class, primary = false)
public interface KeycloakAdminClient {
    String REALM = "realm";
    String SEARCH = "search";
    String EXACT = "exact";
    String USERNAME = "username";
    String REDIRECT_URI = "redirect_uri";
    String LIFESPAN = "lifespan";

    // ENDPOINTS
    String USERS_ENDPOINT = "/admin/realms/{realm}/users";
    String USER_ID_ENDPOINT = "/admin/realms/{realm}/users/{id}";
    String EXECUTE_ACTIONS_EMAIL_ENDPOINT = "/admin/realms/{realm}/users/{id}/execute-actions-email";
    String SEND_VERIFY_EMAIL_ENDPOINT = "/admin/realms/{realm}/users/{id}/send-verify-email";

    /**
     * Get users
     *
     * @param realm  realm
     * @param search search query to look in first name, last name, email
     * @return list of users
     */
    @GetMapping(path = USERS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    List<User> getUsers(@PathVariable(value = REALM) String realm,
                        @RequestParam(value = SEARCH) Optional<String> search);

    /**
     * Get users
     *
     * @param realm realm
     * @return all the users
     */
    default List<User> getUsers(@PathVariable(value = REALM) String realm) {
        return getUsers(realm, Optional.empty());
    }

    /**
     * Get user by username
     *
     * @param realm    realm
     * @param username username
     * @param exact    exact match boolean to match the email, false to match the email partially
     * @return user
     */
    @GetMapping(path = USERS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    List<User> getUserByUsername(@PathVariable(value = REALM) String realm,
                           @RequestParam(value = USERNAME) String username,
                           @RequestParam(value = EXACT) Boolean exact);


    /**
     * Get user by username
     *
     * @param realm    realm
     * @param username username to search
     * @return user
     */
    default User getUserByUsername(@PathVariable(value = REALM) String realm,
                                   @RequestParam(value = USERNAME) String username) {
        return getUserByUsername(realm, username, true).get(0);
    }

    /**
     * Get user by id
     *
     * @param realm realm
     * @param id    user id
     * @return user
     */
    @GetMapping(path = USER_ID_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    User getUserById(@PathVariable(value = REALM) String realm,
                     @PathVariable(value = JsonAttributes.ID) String id);


    /**
     * Update user
     *
     * @param realm realm
     * @param id    user id
     * @param user  user to update
     */
    @PutMapping(path = USER_ID_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void updateUser(@PathVariable(value = REALM) String realm,
                    @PathVariable(value = JsonAttributes.ID) String id,
                    @RequestBody User user);

    /**
     * Create user
     *
     * @param realm realm
     * @param id    user id
     */
    @DeleteMapping(path = USER_ID_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteUser(@PathVariable(value = REALM) String realm,
                    @PathVariable(value = JsonAttributes.ID) String id);


    /**
     * Send verify email
     *
     * @param realm       realm
     * @param id          user id
     * @param redirectUri redirect uri
     * @param lifespan    lifespan
     */
    @PutMapping(path = SEND_VERIFY_EMAIL_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void sendVerifyEmail(@PathVariable(value = REALM) String realm,
                         @PathVariable(value = JsonAttributes.ID) String id,
                         @RequestParam(value = REDIRECT_URI) Optional<String> redirectUri,
                         @RequestParam(value = LIFESPAN) Optional<Integer> lifespan);


    /**
     * Reset user password
     *
     * @param realm realm
     * @param id    user id
     */
    @PutMapping(path = EXECUTE_ACTIONS_EMAIL_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void sendActionsEmail(@PathVariable(value = REALM) String realm,
                          @PathVariable(value = JsonAttributes.ID) String id,
                          @RequestBody List<String> actions);

    /**
     * Create user
     *
     * @param realm realm
     * @param user  user to create
     * @return created user
     */
    @PostMapping(path = USERS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    User createUser(@PathVariable(value = REALM) String realm,
                    @RequestBody User user);
}
