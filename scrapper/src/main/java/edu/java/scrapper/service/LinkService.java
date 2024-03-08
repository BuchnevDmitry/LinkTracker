package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.handler.link.HandlerLinkFacade;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.java.scrapper.model.response.LinkResponse;
import edu.java.scrapper.model.response.ListLinksResponse;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    private final HandlerLinkFacade handlerLinkFacade;

    private final Map<Long, List<LinkResponse>> links = new HashMap<>();

    public LinkService(HandlerLinkFacade handlerLinkFacade) {
        this.handlerLinkFacade = handlerLinkFacade;
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        List<LinkResponse> linkResponses = links.getOrDefault(tgChatId, new ArrayList<>());
        return new ListLinksResponse(
            links.getOrDefault(tgChatId, new ArrayList<>()),
            linkResponses.size()
            );
    }

    public LinkResponse addLink(Long tgChatId, URI uri) {
        List<LinkResponse> linkList = links.getOrDefault(tgChatId, new ArrayList<>());
        LinkResponse link = new LinkResponse(1L, uri);
        if (!chatExists(tgChatId, uri)) {
            if (handlerLinkFacade.getChainHead().handle(uri.toString())) {
                linkList.add(link);
                links.put(tgChatId, linkList);
                return link;
            } else {
                throw new BadRequestException("Данную ссылку невозможно обработать");
            }
        } else {
            throw new ResourceAlreadyExistsException("Ссылка уже добавлена");
        }
    }

    public LinkResponse deleteLink(Long tgChatId, URI uri) {
        LinkResponse link = new LinkResponse(1L, uri);
        if (chatExists(tgChatId, uri)) {
            List<LinkResponse> linkList = links.get(tgChatId);
            linkList.remove(link);
            return link;
        } else {
            throw new NotFoundException("Ссылка не найдена");
        }
    }

    public boolean chatExists(Long tgChatId, URI uri) {
        List<LinkResponse> linkList = links.getOrDefault(tgChatId, new ArrayList<>());
        return linkList.contains(new LinkResponse(1L, uri));
    }
}
