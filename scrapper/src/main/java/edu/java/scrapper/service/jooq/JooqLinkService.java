package edu.java.scrapper.service.jooq;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import edu.java.scrapper.model.response.ChatResponse;
import edu.java.scrapper.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JooqLinkService implements LinkService {
    private final HandlerLinkFacade handlerLinkFacade;

    private final LinkRepository linkRepository;

    public JooqLinkService(HandlerLinkFacade handlerLinkFacade,
        @Qualifier("jooqLinkRepository") LinkRepository linkRepository) {
        this.handlerLinkFacade = handlerLinkFacade;
        this.linkRepository = linkRepository;
    }

    @Override
    public List<Link> findAll(OffsetDateTime criteria) {
        return linkRepository.findAll(criteria);
    }

    @Override
    public List<Link> getLinks(Long chatId) {
        return linkRepository.findLinks(chatId);
    }

    @Override
    public List<ChatResponse> getChats(Long linkId) {
        return linkRepository.findChats(linkId);
    }

    @Override
    public Link addLink(Long chatId, AddLinkRequest link) {
        String url = link.url().toString();
        if (!linkRepository.exist(url)) {
            HandlerData handlerData = handlerLinkFacade.getChainHead().handle(url);
            linkRepository.add(link, handlerData.hash());
            Link linkByUrl = getByUrl(url);
            linkRepository.addLinkToChat(chatId, linkByUrl.id());
            return linkByUrl;
        } else {
            Link linkByUrl = getByUrl(url);
            if (!exist(chatId, linkByUrl.id())) {
                linkRepository.addLinkToChat(chatId, linkByUrl.id());
                return linkByUrl;
            }
            throw new ResourceAlreadyExistsException("Ссылка уже добавлена");
        }
    }

    @Override
    public Link deleteLink(Long chatId, RemoveLinkRequest link) {
        Link linkByUrl = getByUrl(link.url().toString());
        if (exist(chatId, linkByUrl.id())) {
            linkRepository.removeLinkToChat(chatId, linkByUrl.id());
            if (!linkRepository.existLinkToChatByLinkId(linkByUrl.id())) {
                linkRepository.remove(linkByUrl.id());
            }
            return linkByUrl;
        } else {
            throw new NotFoundException("Ссылка в данном чате не найдена");
        }
    }

    @Override
    public boolean exist(Long chatId, Long linkId) {
        return linkRepository.existLinkToChat(chatId, linkId);
    }

    @Override
    public Link getByUrl(String url) {
        return linkRepository.findByUrl(url).orElseThrow(() -> new NotFoundException("Ссылка с таким url не найдена"));
    }

    @Override
    public void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash) {
        linkRepository.updateLink(linkId, lastCheckTime, hash);
    }
}