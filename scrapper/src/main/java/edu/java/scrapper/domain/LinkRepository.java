package edu.java.scrapper.domain;

import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAllByLastCheckTimeBefore(OffsetDateTime time);

    void add(AddLinkRequest link, Integer hash);

    void remove(Long linkId);

    Optional<Link> findByUrl(String url);

    boolean exists(String url);

    boolean existsLinkToChatByLinkId(Long linkId);

    void addLinkToChat(Long chatId, Long linkId);

    void removeLinkToChat(Long chatId, Long linkId);

    List<Link> findLinks(Long chatId);

    List<Chat> findChats(Long linkId);

    boolean existsLinkToChat(Long chatId, Long linkId);

    void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash);
}
