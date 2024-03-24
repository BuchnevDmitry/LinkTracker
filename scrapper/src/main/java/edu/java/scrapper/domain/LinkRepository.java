package edu.java.scrapper.domain;

import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAll(OffsetDateTime criteria);

    void add(AddLinkRequest link, Integer hash);

    void remove(Long linkId);

    Optional<Link> findByUrl(String url);

    boolean exist(String url);

    boolean existLinkToChatByLinkId(Long linkId);

    void addLinkToChat(Long chatId, Long linkId);

    void removeLinkToChat(Long chatId, Long linkId);

    List<Link> findLinks(Long chatId);

    List<Chat> findChats(Long linkId);

    boolean existLinkToChat(Long chatId, Long linkId);

    void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash);
}
