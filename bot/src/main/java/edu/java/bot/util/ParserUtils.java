package edu.java.bot.util;

public final class ParserUtils {
    public static String parseCommandName(String message) {
        String[] result = message.split("\\s+");
        return result[0];
    }

    public static String parseUrl(String message) {
        String[] result = message.split("\\s+");
        return result[1];
    }
}
