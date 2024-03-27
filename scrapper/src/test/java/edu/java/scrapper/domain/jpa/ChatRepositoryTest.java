package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.model.request.AddChatRequest;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-jpa-test.yml"})
public class ChatRepositoryTest extends IntegrationTest {

    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        List<Chat> chatResponsesBefore = chatRepository.findAll();
        AddChatRequest chat = new AddChatRequest("name");

        Assertions.assertDoesNotThrow(() -> chatRepository.add(1L, chat));
        List<Chat> chatResponsesAfter = chatRepository.findAll();
        Assertions.assertEquals(chatResponsesBefore.size() + 1, chatResponsesAfter.size());

    }

    @Test
    @Transactional
    @Rollback
    void removeChatTest() {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest("name");
        chatRepository.add(id, chat);
        List<Chat> chatResponsesBefore = chatRepository.findAll();
        chatRepository.remove(id);
        List<Chat> chatResponsesAfter = chatRepository.findAll();
        Assertions.assertEquals(chatResponsesBefore.size(), chatResponsesAfter.size() + 1);
    }

    @Test
    @Transactional
    @Rollback
    void existChatTest() {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest("name");
        chatRepository.add(id, chat);
        Assertions.assertTrue(chatRepository.exists(id));
        chatRepository.remove(id);
        Assertions.assertFalse(chatRepository.exists(id));
    }

}
