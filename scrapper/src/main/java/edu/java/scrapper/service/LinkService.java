package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkService {

    private final HandlerLinkFacade handlerLinkFacade;

    private final LinkRepository linkRepository;

    public LinkService(
        HandlerLinkFacade handlerLinkFacade,
        LinkRepository linkRepository
    ) {
        this.handlerLinkFacade = handlerLinkFacade;
        this.linkRepository = linkRepository;
    }

    public List<Link> findAllByLastCheckTimeBefore(OffsetDateTime time) {
        return linkRepository.findAllByLastCheckTimeBefore(time);
    }

    public List<Link> getLinks(Long chatId) {
        return linkRepository.findLinks(chatId);
    }

    public List<Chat> getChats(Long linkId) {
        return linkRepository.findChats(linkId);
    }

    @Transactional
    public Link addLink(Long chatId, AddLinkRequest link) {
        String url = link.url().toString();
        if (!linkRepository.exists(url)) {
            HandlerData handlerData = handlerLinkFacade.getChainHead().handle(url);
            linkRepository.add(link, handlerData.hash());
            Link linkByUrl = getByUrl(url);
            linkRepository.addLinkToChat(chatId, linkByUrl.getId());
            return linkByUrl;
        } else {
            Link linkByUrl = getByUrl(url);
            if (!exists(chatId, linkByUrl.getId())) {
                linkRepository.addLinkToChat(chatId, linkByUrl.getId());
                return linkByUrl;
            }
            throw new ResourceAlreadyExistsException("Ссылка уже добавлена");
        }
    }

    @Transactional
    public Link deleteLink(Long chatId, RemoveLinkRequest link) {
        Link linkByUrl = getByUrl(link.url().toString());
        if (exists(chatId, linkByUrl.getId())) {
            linkRepository.removeLinkToChat(chatId, linkByUrl.getId());
            if (!linkRepository.existsLinkToChatByLinkId(linkByUrl.getId())) {
                linkRepository.remove(linkByUrl.getId());
            }
            return linkByUrl;
        } else {
            throw new NotFoundException("Ссылка в данном чате не найдена");
        }
    }

    public boolean exists(Long chatId, Long linkId) {
        return linkRepository.existsLinkToChat(chatId, linkId);
    }

    public Link getByUrl(String url) {
        return linkRepository.findByUrl(url).orElseThrow(() -> new NotFoundException("Ссылка с таким url не найдена"));
    }

    @Transactional
    public void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash) {
        linkRepository.updateLink(linkId, lastCheckTime, hash);
    }

}
