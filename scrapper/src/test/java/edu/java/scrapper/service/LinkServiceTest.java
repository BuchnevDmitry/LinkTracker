package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.handler.link.HandlerLink;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.LinkStatus;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;

@ExtendWith(MockitoExtension.class)
public class LinkServiceTest {
    @InjectMocks
    private LinkService linkService;
    @Mock
    private LinkRepository linkRepository;
    @Mock
    private HandlerLinkFacade handlerLinkFacade;

    @Test
    void addLinkWithLinkNotExistTest() throws URISyntaxException {
        Long chatId = 123L;
        String url = "https://github.com/BuchnevDmitry/testRep";
        AddLinkRequest link = new AddLinkRequest(new URI(url), "username");
        HandlerData handlerData = new HandlerData(1000, LinkStatus.NOT_UPDATE, "desc");
        Mockito.when(linkRepository.exists(link.url().toString())).thenReturn(false);
        Mockito.when(handlerLinkFacade.getChainHead()).thenReturn(Mockito.mock(HandlerLink.class));
        Mockito.when(handlerLinkFacade.getChainHead().handle(url)).thenReturn(handlerData);
        Mockito.doNothing().when(linkRepository).add(link, handlerData.hash());
        Link linkResult = new Link(1L, link.url(), OffsetDateTime.now(), OffsetDateTime.now(), "username", 1000);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(linkResult));
        Mockito.doNothing().when(linkRepository).addLinkToChat(chatId, linkResult.id());
        Link linkReturnService = linkService.addLink(chatId, link);
        Assertions.assertEquals(linkResult, linkReturnService);
    }

    @Test
    void addLinkWithLinkExistTest() throws URISyntaxException {
        Long chatId = 123L;
        String url = "https://github.com/BuchnevDmitry/testRep";
        AddLinkRequest link = new AddLinkRequest(new URI(url), "username");
        Mockito.when(linkRepository.exists(link.url().toString())).thenReturn(true);
        Link linkResult = new Link(1L, link.url(), OffsetDateTime.now(), OffsetDateTime.now(), "username", 1000);
        Mockito.when(linkRepository.existsLinkToChat(chatId, linkResult.id())).thenReturn(false);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(linkResult));
        Mockito.doNothing().when(linkRepository).addLinkToChat(chatId, linkResult.id());
        Link linkReturnService = linkService.addLink(chatId, link);
        Assertions.assertEquals(linkResult, linkReturnService);
    }

    @Test
    void addLinkWithLinkToChatExistTest() throws URISyntaxException {
        Long chatId = 123L;
        String url = "https://github.com/BuchnevDmitry/testRep";
        AddLinkRequest link = new AddLinkRequest(new URI(url), "username");
        Mockito.when(linkRepository.exists(link.url().toString())).thenReturn(true);
        Link linkResult = new Link(1L, link.url(), OffsetDateTime.now(), OffsetDateTime.now(), "username", 1000);
        Mockito.when(linkRepository.existsLinkToChat(chatId, linkResult.id())).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(linkResult));
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> linkService.addLink(chatId, link));
    }

    @Test
    void deleteLinkIfExist() throws URISyntaxException {
        Long chatId = 123L;
        String url = "https://github.com/BuchnevDmitry/testRep";
        RemoveLinkRequest link = new RemoveLinkRequest(new URI(url));
        Link linkResult = new Link(1L, link.url(), OffsetDateTime.now(), OffsetDateTime.now(), "username", 1000);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(linkResult));
        Mockito.when(linkRepository.existsLinkToChat(chatId, linkResult.id())).thenReturn(true);
        Assertions.assertEquals(linkResult, linkService.deleteLink(chatId, link));
    }

    @Test
    void deleteLinkIfNotExist() throws URISyntaxException {
        Long chatId = 123L;
        String url = "https://github.com/BuchnevDmitry/testRep";
        RemoveLinkRequest link = new RemoveLinkRequest(new URI(url));
        Link linkResult = new Link(1L, link.url(), OffsetDateTime.now(), OffsetDateTime.now(), "username", 1000);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(linkResult));
        Mockito.when(linkRepository.existsLinkToChat(chatId, linkResult.id())).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> linkService.deleteLink(chatId, link));
    }


}
