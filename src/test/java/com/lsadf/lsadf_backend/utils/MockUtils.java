package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.mocks.security.LsadfUserDetailsServiceMock;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.services.UserEntityService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

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
     * @param lsadfUserDetailsService the UserDetailsService mock
     */
    public static void initUserDetailsServiceMock(UserDetailsService lsadfUserDetailsService,
                                                  UserEntityService userEntityService) throws NotFoundException {
        Mockito.reset(lsadfUserDetailsService);

        LsadfUserDetailsServiceMock userDetailsServiceMock = new LsadfUserDetailsServiceMock(userEntityService);

        when(lsadfUserDetailsService.loadUserByUsername(Mockito.anyString())).thenAnswer(invocation -> userDetailsServiceMock.loadUserByUsername(invocation.getArgument(0)));
    }

}
