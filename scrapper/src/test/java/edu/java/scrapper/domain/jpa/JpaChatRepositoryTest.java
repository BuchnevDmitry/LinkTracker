package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.model.request.AddChatRequest;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JpaChatRepositoryTest extends IntegrationTest {

    @Autowired
    private JpaChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        List<Chat> chatResponsesBefore = chatRepository.findAll();
        AddChatRequest chatRequest = new AddChatRequest("name");
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setCreatedBy(chatRequest.createdBy());
        Assertions.assertDoesNotThrow(() -> chatRepository.save(chat));
        List<Chat> chatResponsesAfter = chatRepository.findAll();
        Assertions.assertEquals(chatResponsesBefore.size() + 1, chatResponsesAfter.size());

    }

    @Test
    @Transactional
    @Rollback
    void removeChatTest() {
        Long id = 1L;
        AddChatRequest chatRequest = new AddChatRequest("name");
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setCreatedBy(chatRequest.createdBy());
        chatRepository.save(chat);
        List<Chat> chatResponsesBefore = chatRepository.findAll();
        chatRepository.deleteById(id);
        List<Chat> chatResponsesAfter = chatRepository.findAll();
        Assertions.assertEquals(chatResponsesBefore.size(), chatResponsesAfter.size() + 1);
    }

//    @Test
//    @Transactional
//    @Rollback
//    void repeatAddChatTest() {
//        Long id = 1L;
//        AddChatRequest chatRequest = new AddChatRequest( "name");
//        Chat chat = new Chat();
//        chat.setId(1L);
//        chat.setCreatedBy(chatRequest.createdBy());
//        chatRepository.save(chat);
//        Assertions.assertThrows(DuplicateKeyException.class, () -> chatRepository.save(chat));
//    }

    @Test
    @Transactional
    @Rollback
    void existChatTest() {
        Long id = 1L;
        AddChatRequest chatRequest = new AddChatRequest( "name");
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setCreatedBy(chatRequest.createdBy());
        chatRepository.save(chat);
        Assertions.assertTrue(chatRepository.existsById(id));
        chatRepository.deleteById(id);
        Assertions.assertFalse(chatRepository.existsById(id));
    }
}
