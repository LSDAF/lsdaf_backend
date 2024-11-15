package com.lsadf.core.utils;

import com.lsadf.core.constants.LogColor;
import lombok.experimental.UtilityClass;

/**
 * Utility class for colorizing strings.
 */
@UtilityClass
public class ColorUtils {
    /**
     * Colorizes the given message with the given color.
     * @param message the message to colorize
     * @param color the color to use
     * @return the colorized message
     */
    public static String colorizeString(String message, LogColor color) {
        return color.getCode() +
                message +
                LogColor.RESET.getCode();
    }
}
