package edu.java.scrapper.domain;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.model.Chat;
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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class LinkRepositoryTest extends IntegrationTest {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addLinkTest() throws URISyntaxException {
        String url = "url";
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        Assertions.assertDoesNotThrow(() -> linkRepository.add(link, 1));
        Assertions.assertTrue(linkRepository.exists(url));
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkTest() throws URISyntaxException {
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        linkRepository.add(link, 1);
        Assertions.assertTrue(linkRepository.exists(link.url().toString()));
        Link linkFind = linkRepository.findByUrl(link.url().toString()).orElseThrow(() -> new RuntimeException("В базе нет такого url"));
        linkRepository.remove(linkFind.getId());
        Assertions.assertFalse(linkRepository.exists(link.url().toString()));
    }

    @Test
    @Transactional
    @Rollback
    void linkToChatTest() throws URISyntaxException {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest("name");
        chatRepository.add(id, chat);
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        linkRepository.add(link, 1);
        Link linkFind = linkRepository.findByUrl(link.url().toString()).orElseThrow(() -> new RuntimeException("В базе нет такого url"));
        linkRepository.addLinkToChat(id, linkFind.getId());
        Assertions.assertTrue(linkRepository.existsLinkToChat(id, linkFind.getId()));
        linkRepository.removeLinkToChat(id, linkFind.getId());
        Assertions.assertFalse(linkRepository.existsLinkToChat(id, linkFind.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void findLinksTest() throws URISyntaxException {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest( "name");
        chatRepository.add(id, chat);
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        linkRepository.add(link, 1);
        List<Link> linksBefore = linkRepository.findLinks(id);
        Link linkFind = linkRepository.findByUrl(link.url().toString()).orElseThrow(() -> new RuntimeException("В базе нет такого url"));
        linkRepository.addLinkToChat(id, linkFind.getId());
        List<Link> linksAfter = linkRepository.findLinks(id);
        Assertions.assertEquals(linksAfter.size() , linksBefore.size() + 1);
    }

    @Test
    @Transactional
    @Rollback
    void findByUrlTest() throws URISyntaxException {
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        Assertions.assertDoesNotThrow(() -> linkRepository.add(link, 1));
        Optional<Link> linkByUrlBefore = linkRepository.findByUrl(link.url().toString());
        Assertions.assertTrue(linkByUrlBefore.isPresent());
        linkRepository.remove(linkByUrlBefore.get().getId());
        Optional<Link> linkByUrlAfter = linkRepository.findByUrl(link.url().toString());
        Assertions.assertFalse(linkByUrlAfter.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void findChatsTest() throws URISyntaxException {
        Long id = 1L;
        AddChatRequest chat = new AddChatRequest( "name");
        chatRepository.add(id, chat);
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        linkRepository.add(link, 1);
        Optional<Link> linkByUrl = linkRepository.findByUrl(link.url().toString());
        List<Chat> chatsBefore = linkRepository.findChats(linkByUrl.get().getId());
        linkRepository.addLinkToChat(id, linkByUrl.get().getId());
        List<Chat> chatsAfter = linkRepository.findChats(linkByUrl.get().getId());
        Assertions.assertEquals(chatsAfter.size() , chatsBefore.size() + 1);
    }

    @Test
    void updateLinkTest() throws URISyntaxException {
        AddLinkRequest link = new AddLinkRequest(new URI("url"), "name");
        linkRepository.add(link, 1);
        Link linkByUrlBefore = linkRepository.findByUrl(link.url().toString()).get();
        linkRepository.updateLink(linkByUrlBefore.getId(), OffsetDateTime.now(), 2);
        Link linkByUrlAfter = linkRepository.findByUrl(link.url().toString()).get();
        boolean condition = linkByUrlAfter.equals(linkByUrlBefore);
        Assertions.assertFalse(condition);
    }


    @Test
    void findAllByCriteriaTest() throws URISyntaxException {
        AddLinkRequest link1 = new AddLinkRequest(new URI("url1"), "name1");
        AddLinkRequest link2 = new AddLinkRequest(new URI("url2"), "name2");
        AddLinkRequest link3 = new AddLinkRequest(new URI("url3"), "name3");
        linkRepository.add(link1, 1);
        linkRepository.add(link2, 2);
        linkRepository.add(link3, 3);
        Link linkByUrl2 = linkRepository.findByUrl(link2.url().toString()).get();
        Link linkByUrl3 = linkRepository.findByUrl(link3.url().toString()).get();
        OffsetDateTime time = OffsetDateTime.now();
        linkRepository.updateLink(linkByUrl2.getId(), time, 1);
        linkRepository.updateLink(linkByUrl3.getId(), time, 1);
        List<Link> links = linkRepository.findAllByLastCheckTimeBefore(time);
        Assertions.assertEquals(1, links.size());
    }


    @Test
    @Transactional
    @Rollback
    void linkExistTest() throws URISyntaxException {
        Long id = 1L;
        String url = "url";
        AddChatRequest chat = new AddChatRequest( "name");
        chatRepository.add(id, chat);
        AddLinkRequest link = new AddLinkRequest(new URI(url), "name");
        linkRepository.add(link, 1);
        Link linkByUrl = linkRepository.findByUrl(link.url().toString()).get();
        Assertions.assertTrue(linkRepository.exists(url));
        Assertions.assertDoesNotThrow(() -> linkRepository.addLinkToChat(id, linkByUrl.getId()));
        Assertions.assertTrue(linkRepository.existsLinkToChatByLinkId(linkByUrl.getId()));
    }
}
