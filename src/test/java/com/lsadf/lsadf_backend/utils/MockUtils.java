package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.config.mocks.impl.*;
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
        when(userRepository.existsById(any())).thenAnswer(invocation -> userRepositoryMock.existsById(invocation.getArgument(0)));
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
            userRepositoryMock.deleteById(invocation.getArgument(0));
            return null;
        }).when(userRepository).deleteById(Mockito.anyString());
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
     * @param gameSaveRepository the GameSaveRepository mock
     */
    public static void initGameSaveRepositoryMock(GameSaveRepository gameSaveRepository,
                                                  CurrencyRepository currencyRepository) {
        Mockito.reset(gameSaveRepository);

        GameSaveRepositoryMock gameSaveRepositoryMock = new GameSaveRepositoryMock(currencyRepository);
        when(gameSaveRepository.findAll()).thenReturn(gameSaveRepositoryMock.findAll());
        when(gameSaveRepository.count()).thenReturn(gameSaveRepositoryMock.count());
        when(gameSaveRepository.findAllGameSaves()).thenReturn(gameSaveRepositoryMock.findAllSaveGames());
        when(gameSaveRepository.findGameSaveEntitiesByUserEmail(Mockito.anyString())).thenAnswer(invocation -> gameSaveRepositoryMock.findGameSaveEntitiesByUserEmail(invocation.getArgument(0)));
        when(gameSaveRepository.existsById(any())).thenAnswer(invocation -> gameSaveRepositoryMock.existsById(invocation.getArgument(0)));
        when(gameSaveRepository.findById(Mockito.anyString())).thenAnswer(invocation -> gameSaveRepositoryMock.findById(invocation.getArgument(0)));
        when(gameSaveRepository.save(Mockito.any())).thenAnswer(invocation -> gameSaveRepositoryMock.save(invocation.getArgument(0)));
        when(gameSaveRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> gameSaveRepositoryMock.saveAll(invocation.getArgument(0)));
        doAnswer(invocation -> {
            gameSaveRepositoryMock.clear();
            return null;
        }).when(gameSaveRepository).deleteAll();
        doAnswer(invocation -> {
            gameSaveRepositoryMock.deleteById(invocation.getArgument(0));
            return null;
        }).when(gameSaveRepository).deleteById(Mockito.anyString());

    }

    /**
     * Initialize the GoldRepository mock
     *
     * @param currencyRepository the GoldRepository mock
     */
    public static void initCurrencyRepositoryMock(CurrencyRepository currencyRepository) {
        Mockito.reset(currencyRepository);
        CurrencyRepositoryMock currencyRepositoryMock = new CurrencyRepositoryMock();
        when(currencyRepository.findAll()).thenReturn(currencyRepositoryMock.findAll());
        when(currencyRepository.count()).thenReturn(currencyRepositoryMock.count());
        when(currencyRepository.existsById(any())).thenAnswer(invocation -> currencyRepositoryMock.existsById(invocation.getArgument(0)));
        when(currencyRepository.findById(Mockito.anyString())).thenAnswer(invocation -> currencyRepositoryMock.findById(invocation.getArgument(0)));
        when(currencyRepository.save(Mockito.any())).thenAnswer(invocation -> currencyRepositoryMock.save(invocation.getArgument(0)));
        when(currencyRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> currencyRepositoryMock.saveAll(invocation.getArgument(0)));
        doAnswer(invocation -> {
            currencyRepositoryMock.clear();
            return null;
        }).when(currencyRepository).deleteAll();
        doAnswer(invocation -> {
            currencyRepositoryMock.deleteById(invocation.getArgument(0));
            return null;
        }).when(currencyRepository).deleteById(Mockito.anyString());
    }

    /**
     * Initialize the RefreshTokenRepository mock
     * @param refreshTokenRepository the RefreshTokenRepository mock
     */
    public static void initRefreshTokenRepository(RefreshTokenRepository refreshTokenRepository,
                                                  UserRepository userRepository) {
        Mockito.reset(refreshTokenRepository);
        RefreshTokenRepositoryMock refreshTokenRepositoryMock = new RefreshTokenRepositoryMock(userRepository);
        when(refreshTokenRepository.findAll()).thenReturn(refreshTokenRepositoryMock.findAll());
        when(refreshTokenRepository.count()).thenReturn(refreshTokenRepositoryMock.count());
        when(refreshTokenRepository.existsById(any())).thenAnswer(invocation -> refreshTokenRepositoryMock.existsById(invocation.getArgument(0)));
        when(refreshTokenRepository.findById(Mockito.any())).thenAnswer(invocation -> refreshTokenRepositoryMock.findById(invocation.getArgument(0)));
        when(refreshTokenRepository.save(Mockito.any())).thenAnswer(invocation -> refreshTokenRepositoryMock.save(invocation.getArgument(0)));
        when(refreshTokenRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> refreshTokenRepositoryMock.saveAll(invocation.getArgument(0)));
        doAnswer(invocation -> {
            refreshTokenRepositoryMock.clear();
            return null;
        }).when(refreshTokenRepository).deleteAll();
        doAnswer(invocation -> {
            refreshTokenRepositoryMock.deleteById(invocation.getArgument(0));
            return null;
        }).when(refreshTokenRepository).deleteById(Mockito.anyString());
        when(refreshTokenRepository.findByUserAndStatus(Mockito.any(), Mockito.any())).thenAnswer(invocation -> refreshTokenRepositoryMock.findByUserEmailAndStatus(invocation.getArgument(0), invocation.getArgument(1)));
        when(refreshTokenRepository.findByToken(Mockito.anyString())).thenAnswer(invocation -> refreshTokenRepositoryMock.findByToken(invocation.getArgument(0)));
        when(refreshTokenRepository.findAllByStatus(Mockito.any())).thenAnswer(invocation -> refreshTokenRepositoryMock.findAllByStatus(invocation.getArgument(0)));
    }
}
