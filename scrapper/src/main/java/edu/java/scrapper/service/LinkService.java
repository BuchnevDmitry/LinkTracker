package edu.java.scrapper.service;

import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {

    List<Link> findAll(OffsetDateTime criteria);

    List<Link> getLinks(Long chatId);

    List<Chat> getChats(Long linkId);

    Link addLink(Long chatId, AddLinkRequest link);

    Link deleteLink(Long chatId, RemoveLinkRequest link);

    boolean exist(Long chatId, Long linkId);

    Link getByUrl(String url);

    void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash);
}
