package edu.java.scrapper.domain;

import edu.java.scrapper.ContainerTest;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.model.request.AddLinkRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootTest
public class JdbcLinkRepositoryTest extends ContainerTest {

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addLinkTest() throws URISyntaxException {
        List<Link> linkResponsesBefore = linkRepository.findAll();
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        Assertions.assertDoesNotThrow(() -> linkRepository.add(link));
        List<Link> linkResponsesAfter = linkRepository.findAll();
        Assertions.assertEquals(linkResponsesBefore.size() + 1, linkResponsesAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkTest() throws URISyntaxException {
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        linkRepository.add(link);
        List<Link> linkResponsesBefore = linkRepository.findAll();
        Link linkFind = linkRepository.findByUrl(link.url().toString()).orElseThrow(() -> new RuntimeException("В базе нет такого url"));
        linkRepository.remove(linkFind.id());
        List<Link> linkResponsesAfter = linkRepository.findAll();
        Assertions.assertEquals(linkResponsesBefore.size(), linkResponsesAfter.size() + 1);
    }

    @Test
    @Transactional
    @Rollback
    void existLinkTest() throws URISyntaxException {
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        linkRepository.add(link);
        Link linkFind = linkRepository.findByUrl(link.url().toString()).orElseThrow(() -> new RuntimeException("В базе нет такого url"));
        Assertions.assertTrue(linkRepository.exist(linkFind.id()));
        linkRepository.remove(linkFind.id());
        Assertions.assertFalse(linkRepository.exist(linkFind.id()));
    }

    @Test
    @Transactional
    @Rollback
    void linkToChatTest() throws URISyntaxException {
        AddChatRequest chat = new AddChatRequest(1L, "name");
        chatRepository.add(chat);
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        linkRepository.add(link);
        Link linkFind = linkRepository.findByUrl(link.url().toString()).orElseThrow(() -> new RuntimeException("В базе нет такого url"));
        linkRepository.addLinkToChat(chat.id(), linkFind.id());
        Assertions.assertTrue(linkRepository.existLinkToChat(chat.id(), linkFind.id()));
        linkRepository.removeLinkToChat(chat.id(), linkFind.id());
        Assertions.assertFalse(linkRepository.existLinkToChat(chat.id(), linkFind.id()));
    }

}
