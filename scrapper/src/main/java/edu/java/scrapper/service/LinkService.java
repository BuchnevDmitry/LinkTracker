package edu.java.scrapper.service;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import java.util.List;

public interface LinkService {
    List<Link> getLinks(Long chatId);

    Link addLink(Long chatId, AddLinkRequest link);

    Link deleteLink(Long chatId, RemoveLinkRequest link);

    boolean exist(Long chatId, Long linkId);

    Link getByUrl(String url);
}
