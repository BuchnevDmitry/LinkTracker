package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import edu.java.scrapper.model.response.ChatResponse;
import edu.java.scrapper.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcLinkService implements LinkService {

    private final HandlerLinkFacade handlerLinkFacade;

    private final LinkRepository linkRepository;

    public JdbcLinkService(HandlerLinkFacade handlerLinkFacade, LinkRepository linkRepository) {
        this.handlerLinkFacade = handlerLinkFacade;
        this.linkRepository = linkRepository;
    }

    @Override
    @Transactional
    public List<Link> findAll(OffsetDateTime criteria) {
        return linkRepository.findAll(criteria);
    }

    @Override
    @Transactional
    public List<Link> getLinks(Long chatId) {
        return linkRepository.findLinks(chatId);
    }

    @Override
    @Transactional
    public List<ChatResponse> getChats(Long linkId) {
        return linkRepository.findChats(linkId);
    }

    @Override
    @Transactional
    public Link addLink(Long chatId, AddLinkRequest link) {
        String url = link.url().toString();
        if (!linkRepository.exist(url)) {
            Integer hash = handlerLinkFacade.getChainHead().handle(url);
            linkRepository.add(link, hash);
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
    @Transactional
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
    @Transactional
    public boolean exist(Long chatId, Long linkId) {
        return linkRepository.existLinkToChat(chatId, linkId);
    }

    @Override
    @Transactional
    public Link getByUrl(String url) {
        return linkRepository.findByUrl(url).orElseThrow(() -> new NotFoundException("Ссылка с таким url не найдена"));
    }

    @Override
    @Transactional
    public void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash) {
        linkRepository.updateLink(linkId, lastCheckTime, hash);
    }

}
