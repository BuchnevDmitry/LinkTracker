package edu.java.bot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParseTest {
    @Test
    void parseCommandName_getValidCommandName_whenCommandNameIsExist() {
        String message = "/start";
        String commandName = ParserUtil.parseCommandName(message);
        Assertions.assertEquals(message, commandName);
    }

    @Test
    void parseCommandName_getNullExceptionCommandName_whenCommandNameIsNotExist() {
        String message = null;
        Assertions.assertThrows(NullPointerException.class, () -> ParserUtil.parseCommandName(message));
    }

    @Test
    void parseUrl_getValidUrl_whenUrlIsExist() {
        String url = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
        String message = String.format("/track %s", url);
        Assertions.assertEquals(url, ParserUtil.parseUrl(message));
    }
    @Test
    void parseUrl_getValidUrl_whenUrlIsNotExist() {
        String url = "";
        String message = String.format("/track %s", url);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> ParserUtil.parseUrl(message));
    }

}
