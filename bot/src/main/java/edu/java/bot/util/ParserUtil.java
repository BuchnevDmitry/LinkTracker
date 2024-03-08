package edu.java.bot.util;

import edu.java.bot.api.exception.ParseException;

public final class ParserUtil {

    public static final String WHITESPACE_REGEX = "\\s+";

    private ParserUtil() {
    }

    public static String parseCommandName(String message) {
        try {
            String[] result = message.split(WHITESPACE_REGEX);
            return result[0];
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new ParseException("Ошибка парсинга url");
        }
    }

    public static String parseUrl(String message) {
        try {
            String[] result = message.split(WHITESPACE_REGEX);
            return result[1];
        }
        catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new ParseException("Ошибка парсинга url");
        }
    }
}
