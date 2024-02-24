package edu.java.bot.сommand;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.impl.HelpCommand;
import edu.java.bot.command.impl.ListCommand;
import edu.java.bot.command.impl.StartCommand;
import edu.java.bot.command.impl.TrackCommand;
import java.util.List;
import java.util.stream.Stream;
import edu.java.bot.command.impl.UntackCommand;
import edu.java.bot.handler.link.BindHandlerLink;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.util.BotUtil.LINK_MISSING;
import static edu.java.bot.util.BotUtil.REGISTRATION;
import static edu.java.bot.util.BotUtil.TRACK_LINKS_NOT_FOUNT;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CommandTest {

    @Test
    void listCommandHandle_shouldValidTextAndParseMode() {
        ListCommand listCommand = new ListCommand();
        Update mockUpdate = mock(Update.class);
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
    @MethodSource("provideUrlForTrackCommandHandle")
    void trackCommandHandle_getValidResponse(String url, String responseText) {
        TrackCommand trackCommand = new TrackCommand(new BindHandlerLink());
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

    private static Stream<Arguments> provideUrlForTrackCommandHandle() {
        return Stream.of(
            Arguments.of("https://github.com/sanyarnd/tinkoff-java-course-2023/", String.format("Отслеживание https://github.com/sanyarnd/tinkoff-java-course-2023/")),
            Arguments.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c", String.format("Отслеживание https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c")),
            Arguments.of("unknownUrl", "Нет обработчика на данный url!"),
            Arguments.of("", LINK_MISSING)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUrlForUntrackCommandHandle")
    void untrackCommandHandle_getValidResponse(String url, String responseText) {
        UntackCommand untackCommand = new UntackCommand();
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().text()).thenReturn(String.format("/untrack %s", url));
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);

        SendMessage response = untackCommand.handle(updateMock);

        Assertions.assertEquals(responseText,
            response.getParameters().get("text")
        );
    }

    private static Stream<Arguments> provideUrlForUntrackCommandHandle() {
        return Stream.of(
            Arguments.of("https://github.com/sanyarnd/tinkoff-java-course-2023/", String.format("Отслеживание https://github.com/sanyarnd/tinkoff-java-course-2023/ прекращено!")),
            Arguments.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c", String.format("Отслеживание https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c прекращено!")),
            Arguments.of("", LINK_MISSING)
        );
    }

    @Test
    void startCommandHandle_getValidResponse() {
        StartCommand startCommand = new StartCommand();
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);

        SendMessage response = startCommand.handle(updateMock);

        Assertions.assertEquals(REGISTRATION, response.getParameters().get("text"));
    }

    @Test
    void helpCommandHandle_getValidResponse() {
        List<Command> commandList = List.of(new ListCommand(), new StartCommand(), new UntackCommand(), new TrackCommand(
            new BindHandlerLink()));
        HelpCommand helpCommand = new HelpCommand(commandList);
        StringBuilder validResponseText = new StringBuilder();
        String delimiter = " -- ";
        commandList.forEach(command -> validResponseText.append(command.command())
            .append(delimiter)
            .append(command.description()).append("\n"));
        validResponseText.append(helpCommand.command()).append(delimiter).append(helpCommand.description()).append("\n");
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);

        SendMessage response = helpCommand.handle(updateMock);

        Assertions.assertEquals(validResponseText.toString(), response.getParameters().get("text"));
    }
}

