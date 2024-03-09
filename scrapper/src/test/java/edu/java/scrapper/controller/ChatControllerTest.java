package edu.java.scrapper.controller;

import edu.java.scrapper.api.controller.ChatController;
import edu.java.scrapper.service.ChatService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    private MockMvc mockMvc;


    @Test
    public void testRegisterChat() throws Exception {
        Long chatId = 123L;

        chatController.registerChat(chatId);

        verify(chatService).registerChat(chatId);

        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/" + chatId))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteChat() throws Exception {
        Long chatId = 123L;

        chatController.deleteChat(chatId);

        verify(chatService).deleteChat(chatId);

        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();

        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/" + chatId))
            .andExpect(status().isOk());
    }
}
