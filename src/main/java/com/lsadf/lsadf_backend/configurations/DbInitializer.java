package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.properties.DbInitProperties;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for initializing the database with the default users.
 */
@Slf4j
public class DbInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final AtomicBoolean isAlreadySetup = new AtomicBoolean(false);

    private final UserService userService;
    private final DbInitProperties dbInitProperties;

    public DbInitializer(UserService userService,
                         DbInitProperties dbInitProperties) {
        this.userService = userService;
        this.dbInitProperties = dbInitProperties;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (isAlreadySetup.getAndSet(true)) {
            return;
        }

        dbInitProperties.getUsers().forEach(user -> {
            if (!userService.existsByEmail(user.getEmail())) {
                userService.createUser(user.getEmail(),
                        user.getPassword(),
                        SocialProvider.LOCAL,
                        user.getRoles(),
                        user.getName(),
                        true);
            }
        });
    }


}
