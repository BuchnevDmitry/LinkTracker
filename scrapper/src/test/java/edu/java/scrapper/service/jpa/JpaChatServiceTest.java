package edu.java.scrapper.service.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.model.request.AddChatRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JpaChatServiceTest extends IntegrationTest {
    @InjectMocks
    private JpaChatService chatService;

    @Mock
    private JpaChatRepository chatRepository;

    @Test
    void addLinkTest() {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest("name");
        Mockito.when(chatRepository.exist(id)).thenReturn(true);
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> chatService.register(id, chat));
    }

    @Test
    void removeLinkTest() {
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
