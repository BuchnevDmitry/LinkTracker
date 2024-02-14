package edu.java.bot.сommand;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.impl.ListCommand;
import edu.java.bot.command.impl.TrackCommand;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.util.BotUtil.LINK_MISSING;
import static edu.java.bot.util.BotUtil.TRACK_LINKS_NOT_FOUNT;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CommandTest {

    @Mock
    private Update mockUpdate;
    @Test
    void listCommandHandle_shouldValidTextAndParseMode() {
        ListCommand listCommand = new ListCommand();
        mockUpdate = mock(Update.class);
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        Mockito.when(mockUpdate.message()).thenReturn(mockMessage);
        Mockito.when(mockMessage.chat()).thenReturn(mockChat);
        Mockito.when(mockChat.id()).thenReturn(123456L);

        SendMessage result = listCommand.handle(mockUpdate);

        Assertions.assertEquals(result.getParameters().get("text"), TRACK_LINKS_NOT_FOUNT);
        Assertions.assertEquals(result.getParameters().get("parse_mode"), "HTML");

    }
    @ParameterizedTest
    @MethodSource("provideUrlForHandle")
    void handle_getValidResponse(String url, String responseText) {
        TrackCommand trackCommand = new TrackCommand();
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);

        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().text()).thenReturn(String.format("/track %s", url));
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);
        SendMessage response = trackCommand.handle(updateMock);

        Assertions.assertEquals(responseText,
            response.getParameters().get("text")
        );
    }

    private static Stream<Arguments> provideUrlForHandle() {
        return Stream.of(
            Arguments.of("https://github.com/sanyarnd/tinkoff-java-course-2023/", String.format("Отслеживание https://github.com/sanyarnd/tinkoff-java-course-2023/")),
            Arguments.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c", String.format("Отслеживание https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c")),
            Arguments.of("unknownUrl", "Нет обработчика на данный url!"),
            Arguments.of("", LINK_MISSING)
        );
    }
}

