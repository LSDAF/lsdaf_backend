package com.lsadf.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class StringUtils {

    /**
     * Capitalize the first letter of a string
     * @param str the string to capitalize
     * @return the capitalized string
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
