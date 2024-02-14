package edu.java.bot.util;

public final class ParserUtils {

    public static final String WHITESPACE_REGEX = "\\s+";

    private ParserUtils() {
    }

    public static String parseCommandName(String message) {
        String[] result = message.split(WHITESPACE_REGEX);
        return result[0];
    }

    public static String parseUrl(String message) {
        String[] result = message.split(WHITESPACE_REGEX);
        return result[1];
    }
}
