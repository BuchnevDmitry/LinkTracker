package edu.java.scrapper.domain;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAll();

    void add(AddLinkRequest link);

    void remove(Long linkId);

    Optional<Link> findByUrl(String url);
    boolean exist(Long linkId);
    boolean exist(String url);

    boolean existLinkToChatByLinkId(Long linkId);

    void addLinkToChat(Long chatId, Long linkId);

    void removeLinkToChat(Long chatId, Long linkId);

    List<Link> findLinks(Long chatId);

    boolean existLinkToChat(Long chatId, Long linkId);
}
