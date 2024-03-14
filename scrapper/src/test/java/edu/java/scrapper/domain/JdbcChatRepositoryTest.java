package edu.java.scrapper.domain;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        List<ChatResponse> chatResponsesBefore = chatRepository.findAll();
        AddChatRequest chat = new AddChatRequest(1L, "name");
        Assertions.assertDoesNotThrow(() -> chatRepository.add(chat));
        List<ChatResponse> chatResponsesAfter = chatRepository.findAll();
        Assertions.assertEquals(chatResponsesBefore.size() + 1, chatResponsesAfter.size());

    }

    @Test
    @Transactional
    @Rollback
    void removeChatTest() {
        AddChatRequest chat = new AddChatRequest(1L, "name");
        chatRepository.add(chat);
        List<ChatResponse> chatResponsesBefore = chatRepository.findAll();
        chatRepository.remove(chat.id());
        List<ChatResponse> chatResponsesAfter = chatRepository.findAll();
        Assertions.assertEquals(chatResponsesBefore.size(), chatResponsesAfter.size() + 1);
    }

    @Test
    @Transactional
    @Rollback
    void repeatAddChatTest() {
        AddChatRequest chat = new AddChatRequest(1L, "name");
        chatRepository.add(chat);
        Assertions.assertThrows(DuplicateKeyException.class, () -> chatRepository.add(chat));
    }

    @Test
    @Transactional
    @Rollback
    void existChatTest() {
        AddChatRequest chat = new AddChatRequest(1L, "name");
        chatRepository.add(chat);
        Assertions.assertTrue(chatRepository.exist(chat.id()));
        chatRepository.remove(chat.id());
        Assertions.assertFalse(chatRepository.exist(chat.id()));
    }

}
