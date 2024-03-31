package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.exception.NotFoundException;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.client.ScrapperClientTest;
import edu.java.bot.command.impl.StartCommand;
import edu.java.bot.model.request.AddChatRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.util.BotMessages.COMMAND_NOT_FOUND;
import static edu.java.bot.util.BotMessages.REGISTRATION;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserMessageServiceTest {
    @InjectMocks
    private UserMessageService userMessageService;
    @Mock
    private CommandService commandService;
    @Test
    void process_shouldCommandNotFoundMessage_whenCommandIsNotExist() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Chat chatMock = mock(Chat.class);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().text()).thenReturn("/command");
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);

        Mockito.when(commandService.getCommand(updateMock.message().text())).thenThrow(new NotFoundException(COMMAND_NOT_FOUND));
        SendMessage response = userMessageService.process(updateMock);
        Assertions.assertEquals(COMMAND_NOT_FOUND, response.getParameters().get("text"));
    }

    @Test
    void process_shouldRegistrationMessage_whenCommandIsStart() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        User user = mock(User.class);
        Chat chatMock = mock(Chat.class);
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        StartCommand startCommand = new StartCommand(scrapperClient);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().text()).thenReturn("/start");
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);
        Mockito.when(updateMock.message().from()).thenReturn(user);
        Mockito.when(updateMock.message().from().username()).thenReturn("username");
        Mockito.when(commandService.getCommand(updateMock.message().text())).thenReturn(startCommand);
        Mockito.doNothing().when(scrapperClient).registerChat(1L, new AddChatRequest("username"));


        SendMessage response = userMessageService.process(updateMock);

        Assertions.assertEquals(REGISTRATION, response.getParameters().get("text"));
    }
}
