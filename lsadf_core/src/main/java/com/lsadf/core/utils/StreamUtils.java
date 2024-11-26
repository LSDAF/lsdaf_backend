package com.lsadf.core.utils;

import com.lsadf.core.models.GameSave;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.game_save.GameSaveSortingParameter;
import com.lsadf.core.requests.user.UserSortingParameter;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtils {

    /**
     * Apply filters to a stream
     *
     * @param userStream the user stream
     * @param sortingParameters    the sorting parameters
     * @return the filtered stream
     */
    public static Stream<User> sortUsers(Stream<User> userStream, List<UserSortingParameter> sortingParameters) {
        if (sortingParameters == null || sortingParameters.isEmpty()) {
            return userStream;
        }
        // Combine all comparators into a single comparator
        Comparator<User> finalComparator = null;
        boolean initialized = false;
        for (UserSortingParameter orderBy: sortingParameters) {
            Comparator<User> comparator = orderBy.getComparator();
            if (!initialized) {
                finalComparator = comparator;
                initialized = true;
            } else {
                finalComparator = finalComparator.thenComparing(comparator);
            }
        }

        if (finalComparator == null) {
            return userStream;
        }

        return userStream.sorted(finalComparator);
    }

    /**
     * Apply sorting filters to a stream
     * @param gameSaveStream the game save stream
     * @param sortingParameters the sorting parameters
     * @return the sorted stream
     */
    public static Stream<GameSave> sortGameSaves(Stream<GameSave> gameSaveStream, List<GameSaveSortingParameter> sortingParameters) {
        if (sortingParameters == null || sortingParameters.isEmpty()) {
            return gameSaveStream;
        }
        // Combine all comparators into a single comparator
        Comparator<GameSave> finalComparator = null;
        boolean initialized = false;
        for (GameSaveSortingParameter orderBy: sortingParameters) {
            Comparator<GameSave> comparator = orderBy.getComparator();
            if (!initialized) {
                finalComparator = comparator;
                initialized = true;
            } else {
                finalComparator = finalComparator.thenComparing(comparator);
            }
        }

        if (finalComparator == null) {
            return gameSaveStream;
        }

        return gameSaveStream.sorted(finalComparator);
    }
}
