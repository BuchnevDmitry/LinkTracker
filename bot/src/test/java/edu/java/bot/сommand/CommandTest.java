package edu.java.bot.сommand;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.command.impl.HelpCommand;
import edu.java.bot.command.impl.ListCommand;
import edu.java.bot.command.impl.StartCommand;
import edu.java.bot.command.impl.TrackCommand;
import edu.java.bot.command.impl.UntackCommand;
import edu.java.bot.model.request.AddChatRequest;
import edu.java.bot.model.request.AddLinkRequest;
import edu.java.bot.model.request.RemoveLinkRequest;
import edu.java.bot.model.response.LinkResponse;
import edu.java.bot.model.response.ListLinksResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.util.BotMessages.LINK_MISSING;
import static edu.java.bot.util.BotMessages.LINK_WRONG_FORMAT;
import static edu.java.bot.util.BotMessages.REGISTRATION;
import static edu.java.bot.util.BotMessages.TRACK_LINKS_NOT_FOUND;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CommandTest {

    @Test
    void listCommandHandle_shouldValidTextAndParseMode() {
        Update mockUpdate = mock(Update.class);
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        ListCommand listCommand = new ListCommand(scrapperClient);
        Mockito.when(mockUpdate.message()).thenReturn(mockMessage);
        Mockito.when(mockMessage.chat()).thenReturn(mockChat);
        Mockito.when(mockChat.id()).thenReturn(123456L);
        Mockito.when(scrapperClient.getLinks(Mockito.anyLong())).thenReturn(new ListLinksResponse(new ArrayList<>(), 0));

        SendMessage result = listCommand.handle(mockUpdate);

        Assertions.assertEquals(result.getParameters().get("text"), TRACK_LINKS_NOT_FOUND);
        Assertions.assertEquals(result.getParameters().get("parse_mode"), "HTML");

    }
    @ParameterizedTest
    @MethodSource("provideUrlForTrackCommandHandle")
    void trackCommandHandle_getValidResponse(String url, String responseText) {
        LinkResponse linkResponse = null;
        try {
            linkResponse = new LinkResponse(1L, new URI(url));
        } catch (URISyntaxException e) {
            linkResponse = new LinkResponse(1L, Mockito.mock(URI.class));
        }
        ScrapperClient scrapperClient = Mockito.mock(ScrapperClient.class);
        TrackCommand trackCommand = new TrackCommand(scrapperClient);
        Mockito.lenient().when(scrapperClient.addLink(Mockito.anyLong(), Mockito.any(AddLinkRequest.class))).thenReturn(linkResponse);
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        User user = mock(User.class);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().text()).thenReturn(String.format("/track %s", url));
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);
        Mockito.when(updateMock.message().from()).thenReturn(user);
        Mockito.lenient().when(updateMock.message().from().username()).thenReturn("username");

        SendMessage response = trackCommand.handle(updateMock);

        Assertions.assertEquals(responseText,
            response.getParameters().get("text")
        );
    }

    private static Stream<Arguments> provideUrlForTrackCommandHandle() {
        return Stream.of(
            Arguments.of("https://github.com/sanyarnd/tinkoff-java-course-2023/", String.format("Отслеживание id: 1 url: https://github.com/sanyarnd/tinkoff-java-course-2023/")),
            Arguments.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c", String.format("Отслеживание id: 1 url: https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c")),
            Arguments.of("", LINK_MISSING),
            Arguments.of("())()))()&&&&&&&&^^13))___", LINK_WRONG_FORMAT)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUrlForUntrackCommandHandle")
    void untrackCommandHandle_getValidResponse(String url, String responseText) {
        LinkResponse linkResponse = null;
        try {
            linkResponse = new LinkResponse(1L, new URI(url));
        } catch (URISyntaxException e) {
            linkResponse = new LinkResponse(1L, Mockito.mock(URI.class));
        }
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        UntackCommand untackCommand = new UntackCommand(scrapperClient);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().text()).thenReturn(String.format("/untrack %s", url));
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);
        Mockito.lenient().when(scrapperClient.deleteLink(Mockito.anyLong(), Mockito.any(RemoveLinkRequest.class))).thenReturn(linkResponse);


        SendMessage response = untackCommand.handle(updateMock);

        Assertions.assertEquals(responseText,
            response.getParameters().get("text")
        );
    }

    private static Stream<Arguments> provideUrlForUntrackCommandHandle() {
        return Stream.of(
            Arguments.of("https://github.com/sanyarnd/tinkoff-java-course-2023/", String.format("Отслеживание id: 1 url: https://github.com/sanyarnd/tinkoff-java-course-2023/ прекращено!")),
            Arguments.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c", String.format("Отслеживание id: 1 url: https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c прекращено!")),
            Arguments.of("", LINK_MISSING),
            Arguments.of("())()))()&&&&&&&&^^13))___", LINK_WRONG_FORMAT)
        );
    }

    @Test
    void startCommandHandle_getValidResponse() {
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        User user = Mockito.mock(User.class);
        ScrapperClient scrapperClient = Mockito.mock(ScrapperClient.class);
        StartCommand startCommand = new StartCommand(scrapperClient);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);
        Mockito.when(updateMock.message().from()).thenReturn(user);
        Mockito.when(updateMock.message().from().username()).thenReturn("username");
        Mockito.doNothing().when(scrapperClient).registerChat(1L, new AddChatRequest("username"));

        SendMessage response = startCommand.handle(updateMock);

        Assertions.assertEquals(REGISTRATION, response.getParameters().get("text"));
    }

    @Test
    void helpCommandHandle_getValidResponse() {
        List<Command> commandList = List.of(new ListCommand(null), new StartCommand(null), new UntackCommand(
            null), new TrackCommand(null));
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

