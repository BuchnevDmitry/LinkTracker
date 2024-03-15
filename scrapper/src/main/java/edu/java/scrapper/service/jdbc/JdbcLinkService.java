package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import edu.java.scrapper.service.LinkService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkService implements LinkService {

    private final HandlerLinkFacade handlerLinkFacade;

    private final LinkRepository linkRepository;

    public JdbcLinkService(HandlerLinkFacade handlerLinkFacade, LinkRepository linkRepository) {
        this.handlerLinkFacade = handlerLinkFacade;
        this.linkRepository = linkRepository;
    }

    @Override
    public List<Link> getLinks(Long chatId) {
        return linkRepository.findLinks(chatId);
    }

    @Override
    public Link addLink(Long chatId, AddLinkRequest link) {
        String url = link.url().toString();
        if (!linkRepository.exist(url)) {
            if (handlerLinkFacade.getChainHead().handle(url)) {
                linkRepository.add(link);
                Link linkByUrl = getByUrl(url);
                linkRepository.addLinkToChat(chatId, linkByUrl.id());
                return linkByUrl;
            } else {
                throw new BadRequestException("Данную ссылку невозможно обработать");
            }
        }
        else {
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
            throw new NotFoundException("Ссылка не найдена");
        }
    }

    @Override
    public boolean exist(Long chatId, Long linkId) {
        return linkRepository.existLinkToChat(chatId, linkId);
    }

    @Override
    public Link getByUrl(String url) {
        return linkRepository.findByUrl(url).orElseThrow(() -> new NotFoundException("Ссылка не найдена"));
    }


}
