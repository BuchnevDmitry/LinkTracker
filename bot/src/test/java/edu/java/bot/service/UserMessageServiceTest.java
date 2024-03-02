package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.impl.StartCommand;
import edu.java.bot.service.impl.UserMessageServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.util.BotUtil.COMMAND_NOT_FOUND;
import static edu.java.bot.util.BotUtil.REGISTRATION;

@ExtendWith(MockitoExtension.class)
public class UserMessageServiceTest {
    @InjectMocks
    private UserMessageServiceImpl userMessageService;
    @Mock
    private CommandService commandService;
    @Test
    void process_shouldCommandNotFoundMessage_whenCommandIsNotExist() {
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().text()).thenReturn("/command");
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);

        Mockito.when(commandService.getCommand(updateMock.message().text())).thenReturn(null);
        SendMessage response = userMessageService.process(updateMock);
        Assertions.assertEquals(COMMAND_NOT_FOUND, response.getParameters().get("text"));
    }

    @Test
    void process_shouldRegistrationMessage_whenCommandIsStart() {
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        ScrapperClient scrapperClient = Mockito.mock(ScrapperClient.class);
        StartCommand startCommand = new StartCommand();
        startCommand.setScrapperClient(scrapperClient);
        Mockito.when(updateMock.message()).thenReturn(messageMock);
        Mockito.when(updateMock.message().text()).thenReturn("/start");
        Mockito.when(updateMock.message().chat()).thenReturn(chatMock);
        Mockito.when(updateMock.message().chat().id()).thenReturn(1L);
        Mockito.when(commandService.getCommand(updateMock.message().text())).thenReturn(startCommand);
        Mockito.doNothing().when(scrapperClient).registerChat(Mockito.anyLong());


        SendMessage response = userMessageService.process(updateMock);

        Assertions.assertEquals(REGISTRATION, response.getParameters().get("text"));
    }
}
