package edu.java.scrapper.controller;

import edu.java.scrapper.api.controller.ChatController;
import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {
    @Mock
    private JdbcChatService chatService;

    @InjectMocks
    private ChatController chatController;

    private MockMvc mockMvc;

    @Test
    public void testRegisterChat() throws Exception {
        Long id = 123L;
        AddChatRequest chat = new AddChatRequest("name");

        chatController.registerChat(id, chat);

        verify(chatService).register(id, chat);
        Mockito.doNothing().when(chatService).register(id, chat);
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "createdBy":"name"
                        }
                        """))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteChat() throws Exception {
        Long chatId = 123L;

        chatController.deleteChat(chatId);

        verify(chatService).unregister(chatId);

        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();

        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/" + chatId))
            .andExpect(status().isOk());
    }
}
