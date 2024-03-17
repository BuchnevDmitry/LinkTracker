package edu.java.scrapper.domain;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jooq.impl.JooqChatRepository;
import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.model.response.ChatResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SpringBootTest
public class JooqChatRepositoryTest extends IntegrationTest {

    @Autowired
    private JooqChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        List<ChatResponse> chatResponsesBefore = chatRepository.findAll();
        AddChatRequest chat = new AddChatRequest("name");

        Assertions.assertDoesNotThrow(() -> chatRepository.add(1L, chat));
        List<ChatResponse> chatResponsesAfter = chatRepository.findAll();
        Assertions.assertEquals(chatResponsesBefore.size() + 1, chatResponsesAfter.size());

    }

    @Test
    @Transactional
    @Rollback
    void removeChatTest() {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest("name");
        chatRepository.add(id, chat);
        List<ChatResponse> chatResponsesBefore = chatRepository.findAll();
        chatRepository.remove(id);
        List<ChatResponse> chatResponsesAfter = chatRepository.findAll();
        Assertions.assertEquals(chatResponsesBefore.size(), chatResponsesAfter.size() + 1);
    }

    @Test
    @Transactional
    @Rollback
    void repeatAddChatTest() {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest( "name");
        chatRepository.add(id, chat);
        Assertions.assertThrows(DuplicateKeyException.class, () -> chatRepository.add(id, chat));
    }

    @Test
    @Transactional
    @Rollback
    void existChatTest() {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest( "name");
        chatRepository.add(id, chat);
        Assertions.assertTrue(chatRepository.exist(id));
        chatRepository.remove(id);
        Assertions.assertFalse(chatRepository.exist(id));
    }
}
