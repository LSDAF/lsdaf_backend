package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.config.mocks.GameSaveRepositoryMock;
import com.lsadf.lsadf_backend.bdd.config.mocks.UserDetailsServiceMock;
import com.lsadf.lsadf_backend.bdd.config.mocks.UserRepositoryMock;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Utility class for initializing mock objects
 */
@UtilityClass
public class MockUtils {
    /**
     * Initialize the UserRepository mock
     *
     * @param userRepository
     */
    public static void initUserRepositoryMock(UserRepository userRepository) {
        Mockito.reset(userRepository);

        UserRepositoryMock userRepositoryMock = new UserRepositoryMock();
        when(userRepository.findAll()).thenReturn(userRepositoryMock.findAll());
        when(userRepository.count()).thenReturn(userRepositoryMock.count());
        when(userRepository.findAllUsers()).thenReturn(userRepositoryMock.findAllUsers());
        when(userRepository.existsById(any())).thenAnswer(invocation -> userRepositoryMock.existsById(invocation.getArgument(0)).isPresent());
        when(userRepository.existsByEmail(any())).thenAnswer(invocation -> userRepositoryMock.findUserEntityByEmail(invocation.getArgument(0)).isPresent());
        when(userRepository.findById(Mockito.anyString())).thenAnswer(invocation -> userRepositoryMock.findById(invocation.getArgument(0)));
        when(userRepository.save(Mockito.any())).thenAnswer(invocation -> userRepositoryMock.save(invocation.getArgument(0)));
        when(userRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> userRepositoryMock.saveAll(invocation.getArgument(0)));
        when(userRepository.findUserEntityByEmail(Mockito.anyString())).thenAnswer(invocation -> userRepositoryMock.findUserEntityByEmail(invocation.getArgument(0)));
        doAnswer(invocation -> {
            userRepositoryMock.clear();
            return null;
        }).when(userRepository).deleteAll();
        doAnswer(invocation -> {
            userRepositoryMock.deleteUserEntityByEmail(invocation.getArgument(0));
            return null;
        }).when(userRepository).deleteUserEntityByEmail(Mockito.anyString());
    }


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

    /**
     * Initialize the GameSaveRepository mock
     *
     * @param gameSaveRepository
     */
    public static void initGameSaveRepositoryMock(GameSaveRepository gameSaveRepository) {
        Mockito.reset(gameSaveRepository);

        GameSaveRepositoryMock gameSaveRepositoryMock = new GameSaveRepositoryMock();
        when(gameSaveRepository.findAll()).thenReturn(gameSaveRepositoryMock.findAll());
        when(gameSaveRepository.count()).thenReturn(gameSaveRepositoryMock.count());
        when(gameSaveRepository.findAllGameSaves()).thenReturn(gameSaveRepositoryMock.findAllSaveGames());
        when(gameSaveRepository.existsById(any())).thenAnswer(invocation -> gameSaveRepositoryMock.existsById(invocation.getArgument(0)).isPresent());
        when(gameSaveRepository.findById(Mockito.anyString())).thenAnswer(invocation -> gameSaveRepositoryMock.findById(invocation.getArgument(0)));
        when(gameSaveRepository.save(Mockito.any())).thenAnswer(invocation -> gameSaveRepositoryMock.save(invocation.getArgument(0)));
        when(gameSaveRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> gameSaveRepositoryMock.saveAll(invocation.getArgument(0)));
    }
}
