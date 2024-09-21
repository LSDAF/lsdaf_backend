package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.config.mocks.*;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.repositories.RefreshTokenRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Utility class for initializing mock objects
 */
@UtilityClass
@Slf4j
public class MockUtils {
    /**
     * Initialize the UserDetailsService mock
     *
     * @param userDetailsService the UserDetailsService mock
     */
    public static void initUserDetailsServiceMock(UserDetailsService userDetailsService,
                                                  UserService userService,
                                                  Mapper mapper) throws NotFoundException {
        Mockito.reset(userDetailsService);

        UserDetailsServiceMock userDetailsServiceMock = new UserDetailsServiceMock(userService, mapper);

        when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenAnswer(invocation -> userDetailsServiceMock.loadUserByUsername(invocation.getArgument(0)));
        when(userDetailsService.loadUserByEmail(Mockito.anyString())).thenAnswer(invocation -> userDetailsServiceMock.loadUserByEmail(invocation.getArgument(0)));
    }
}
