package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.config.mocks.GameSaveRepositoryMock;
import com.lsadf.lsadf_backend.bdd.config.mocks.UserRepositoryMock;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Utility class for initializing mock objects
 */
public class MockUtils {
    /**
     * Initialize the UserRepository mock
     * @param userRepository
     */
    public static void initUserRepositoryMock(UserRepository userRepository) {
        Mockito.reset(userRepository);

        UserRepositoryMock userRepositoryMock = new UserRepositoryMock();
        when(userRepository.findAll()).thenReturn(userRepositoryMock.findAll());
        when(userRepository.count()).thenReturn(userRepositoryMock.count());
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
     * Initialize the GameSaveRepository mock
     * @param gameSaveRepository
     */
    public static void initGameSaveRepositoryMock(GameSaveRepository gameSaveRepository) {
        Mockito.reset(gameSaveRepository);

        GameSaveRepositoryMock gameSaveRepositoryMock = new GameSaveRepositoryMock();
        when(gameSaveRepository.findAll()).thenReturn(gameSaveRepositoryMock.findAll());
        when(gameSaveRepository.count()).thenReturn(gameSaveRepositoryMock.count());
        when(gameSaveRepository.findById(Mockito.anyString())).thenAnswer(invocation -> gameSaveRepositoryMock.findById(invocation.getArgument(0)));
        when(gameSaveRepository.save(Mockito.any())).thenAnswer(invocation -> gameSaveRepositoryMock.save(invocation.getArgument(0)));
        when(gameSaveRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> gameSaveRepositoryMock.saveAll(invocation.getArgument(0)));
    }
}
