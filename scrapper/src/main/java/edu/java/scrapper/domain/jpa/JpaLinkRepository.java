package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long>, LinkRepository {
    boolean existsByUrl(String url);

    @Override
    @Query("SELECT l FROM Link l WHERE l.lastCheckTime < :criteriaTime")
    List<Link> findAll(OffsetDateTime criteriaTime);

    @Override
    default void add(AddLinkRequest linkRequest, Integer hash) {
        Link link = new Link();
        link.setUrl(linkRequest.url().toString());
        link.setCreatedBy(linkRequest.createdBy());
        link.setHashInt(hash);
        this.save(link);
    };

    @Override
    default void remove(Long linkId) {
        this.deleteById(linkId);
    }

    Optional<Link> findByUrl(String url);

    @Override
    default boolean exist(String url) {
        return this.existsByUrl(url);
    }

    @Override
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Chat c JOIN c.links l WHERE l.id = :linkId")
    boolean existLinkToChatByLinkId(Long linkId);

    @Override
    @Modifying
    @Query(value = "INSERT INTO chat_link (chat_id, link_id) VALUES (:chatId, :linkId)", nativeQuery = true)
    void addLinkToChat(Long chatId, Long linkId);

    @Override
    @Modifying
    @Query(value = "DELETE FROM chat_link WHERE chat_id = :chatId AND link_id = :linkId", nativeQuery = true)
    void removeLinkToChat(Long chatId, Long linkId);

    @Override
    @Query("""
        SELECT l
        FROM Chat c
        JOIN c.links cl
        JOIN Link l ON cl.id = l.id
        WHERE c.id = :chatId
        """)
    List<Link> findLinks(Long chatId);

    @Override
    @Query("""
        SELECT c
            FROM Chat c
            JOIN c.links cl
            WHERE cl.id = :linkId
            """)
    List<Chat> findChats(Long linkId);

    @Override
    @Query("""
        SELECT CASE WHEN COUNT(cl) > 0 THEN true ELSE false END
            FROM Chat c
            JOIN c.links cl
            WHERE c.id = :chatId AND cl.id = :linkId
            """)
    boolean existLinkToChat(Long chatId, Long linkId);

    @Override
    default void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash) {
        Link linkById = findById(linkId).orElseThrow();
        Link link = new Link();
        link.setId(linkId);
        link.setUrl(linkById.getUrl());
        link.setCreatedBy(linkById.getCreatedBy());
        link.setHashInt(hash);
        this.save(link);
    }

}
