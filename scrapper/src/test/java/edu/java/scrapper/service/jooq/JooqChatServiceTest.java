package edu.java.scrapper.service.jooq;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.jooq.impl.JooqChatRepository;
import edu.java.scrapper.model.request.AddChatRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JooqChatServiceTest {

    @InjectMocks
    private JooqChatService chatService;

    @Mock
    private JooqChatRepository chatRepository;

    @Test
    void addChatTest() {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest("name");
        Mockito.when(chatRepository.exist(id)).thenReturn(true);
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> chatService.register(id, chat));
    }

    @Test
    void removeChatTest() {
        Long id = 1L;
        Mockito.when(chatRepository.exist(id)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> chatService.unregister(id));
    }

    @Test
    void existTest() {
        Long id = 1L;
        Mockito.when(chatRepository.exist(id)).thenReturn(true);
        Assertions.assertTrue(chatService.exist(id));
    }
}
