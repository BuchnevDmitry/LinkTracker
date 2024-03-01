package edu.java.scrapper.service;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.util.List;

public interface LinkService {
    List<Link> getLinks(Long tgChatId);

    Link addLink(Long tgChatId, URI uri);

    Link deleteLink(Long tgChatId, URI uri);

    boolean existLink(Long tgChatId, URI uri);
}
