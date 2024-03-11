package edu.java.bot.util;

import edu.java.bot.api.exception.ParseException;
import static edu.java.bot.util.BotMessages.ERROR_PARSE_URL;

public final class ParserUtil {

    public static final String WHITESPACE_REGEX = "\\s+";

    private ParserUtil() {
    }

    public static String parseCommandName(String message) {
        try {
            String[] result = message.split(WHITESPACE_REGEX);
            return result[0];
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new ParseException(ERROR_PARSE_URL);
        }
    }

    public static String parseUrl(String message) {
        try {
            String[] result = message.split(WHITESPACE_REGEX);
            return result[1];
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new ParseException(ERROR_PARSE_URL);
        }
    }
}
