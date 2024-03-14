package edu.java.scrapper.domain;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinkRepository {
    private final JdbcClient jdbcClient;

    public JdbcLinkRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Link> findAll() {
        return jdbcClient.sql("SELECT id, url, last_check_time, created_at, created_by FROM link")
            .query(Link.class)
            .list();
    }

    public void add(AddLinkRequest link) {
        jdbcClient.sql("INSERT INTO link(url, last_check_time, created_at, created_by) values(?,?,?,?)")
            .params(List.of(link.url().toString(), OffsetDateTime.now(), OffsetDateTime.now(), link.createdBy()))
            .update();
    }

    public void remove(Long linkId) {
        jdbcClient.sql("DELETE FROM link where id = :id")
            .param("id", linkId)
            .update();
    }

    public Optional<Link> findByUrl(String url) {
        return jdbcClient.sql("SELECT id, url, last_check_time, created_at, created_by FROM link")
            .query(Link.class)
            .optional();
    }

    public boolean exist(Long linkId) {
        return jdbcClient.sql("SELECT EXISTS(SELECT id FROM link WHERE id = :linkId)")
            .param("linkId", linkId)
            .query(Boolean.class)
            .single();
    }

    public void addLinkToChat(Long chatId, Long linkId) {
        jdbcClient.sql("INSERT INTO chat_link(chat_id, link_id) values(?,?)")
            .params(List.of(chatId, linkId))
            .update();
    }

    public void removeLinkToChat(Long chatId, Long linkId) {
        jdbcClient.sql("DELETE FROM chat_link where chat_id = :chatId and link_id = :linkId")
            .param("chatId", chatId)
            .param("linkId", linkId)
            .update();
    }

    public boolean existLinkToChat(Long chatId, Long linkId) {
        return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM chat_link WHERE chat_id = :chatId and link_id = :linkId)")
            .param("chatId", chatId)
            .param("linkId", linkId)
            .query(Boolean.class)
            .single();
    }
}
