package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.properties.DbInitProperties;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class DbInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private AtomicBoolean isAlreadySetup = new AtomicBoolean(false);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DbInitProperties dbInitProperties;

    public DbInitializer(UserService userService,
                         PasswordEncoder passwordEncoder,
                         DbInitProperties dbInitProperties) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
                userService.createUser(user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        SocialProvider.LOCAL,
                        Optional.of(user.getRoles()));
            }
        });
    }


}
