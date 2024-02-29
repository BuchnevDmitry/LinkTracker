package edu.java.scrapper.service.impl;

import edu.java.scrapper.api.exception.AlreadyExistsException;
import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.LinkService;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LinkServiceImpl implements LinkService {
    private final Map<Long, List<Link>> links = new HashMap<>();
    @Override
    public List<Link> getLinks(Long tgChatId) {
        return links.getOrDefault(tgChatId, new ArrayList<>());
    }

    @Override
    public Link addLink(Long tgChatId, URI uri) {
        List<Link> linkList = links.getOrDefault(tgChatId, new ArrayList<>());
        Link link = new Link(1L, uri);
        if (!existLink(tgChatId, uri)) {
            linkList.add(link);
            links.put(tgChatId, linkList);
            return link;
        }
        else {
            throw new AlreadyExistsException("Ссылка уже добавлена");
        }
    }

    @Override
    public Link deleteLink(Long tgChatId, URI uri) {
        Link link = new Link(1L, uri);
        if (existLink(tgChatId, uri)) {
            List<Link> linkList = links.get(tgChatId);
            linkList.remove(link);
            return link;
        }
        else {
            throw new NotFoundException("Ссылка не найдена");
        }
    }

    @Override
    public boolean existLink(Long tgChatId, URI uri) {
        List<Link> linkList = links.getOrDefault(tgChatId, new ArrayList<>());
        return linkList.contains(new Link(1L, uri));
    }
}
