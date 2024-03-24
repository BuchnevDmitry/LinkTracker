package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcClient jdbcClient;

    public JdbcLinkRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Link> findAll(OffsetDateTime criteria) {
        return jdbcClient.sql("SELECT id, url, last_check_time, created_at, created_by, hash_int FROM link "
                + "where last_check_time < :criteriaTime")
            .param("criteriaTime", criteria)
            .query(Link.class)
            .list();
    }

    @Override
    public void add(AddLinkRequest link, Integer hash) {
        jdbcClient.sql("INSERT INTO link(url, last_check_time, created_at, created_by, hash_int) values(?,?,?,?,?)")
            .params(List.of(link.url().toString(), OffsetDateTime.now(), OffsetDateTime.now(), link.createdBy(), hash))
            .update();
    }

    @Override
    public void remove(Long linkId) {
        jdbcClient.sql("DELETE FROM link where id = :id")
            .param("id", linkId)
            .update();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return jdbcClient.sql(
                "SELECT id, url, last_check_time, created_at, created_by, hash_int FROM link where url = :url")
            .param("url", url)
            .query(Link.class)
            .optional();
    }

    @Override
    public boolean exist(String url) {
        return jdbcClient.sql("SELECT EXISTS(SELECT id FROM link WHERE url = :url)")
            .param("url", url)
            .query(Boolean.class)
            .single();
    }

    @Override
    public boolean existLinkToChatByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM chat_link WHERE link_id = :linkId)")
            .param("linkId", linkId)
            .query(Boolean.class)
            .single();
    }

    @Override
    public void addLinkToChat(Long chatId, Long linkId) {
        jdbcClient.sql("INSERT INTO chat_link(chat_id, link_id) values(?,?)")
            .params(List.of(chatId, linkId))
            .update();
    }

    @Override
    public void removeLinkToChat(Long chatId, Long linkId) {
        jdbcClient.sql("DELETE FROM chat_link where chat_id = :chatId and link_id = :linkId")
            .param("chatId", chatId)
            .param("linkId", linkId)
            .update();
    }

    @Override
    public List<Link> findLinks(Long chatId) {
        return jdbcClient.sql(
                "SELECT id, url, last_check_time, created_at, created_by, hash_int "
                    + "FROM chat_link "
                    + "JOIN link ON chat_link.link_id = link.id "
                    + "WHERE chat_link.chat_id = :chatId")
            .param("chatId", chatId)
            .query(Link.class)
            .list();
    }

    @Override
    public List<Chat> findChats(Long linkId) {
        return jdbcClient.sql(
                "SELECT id, created_at, created_by "
                    + "FROM chat_link "
                    + "JOIN chat ON chat_link.chat_id = chat.id "
                    + "WHERE chat_link.link_id = :linkId")
            .param("linkId", linkId)
            .query(Chat.class)
            .list();
    }

    @Override
    public boolean existLinkToChat(Long chatId, Long linkId) {
        return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM chat_link WHERE chat_id = :chatId and link_id = :linkId)")
            .param("chatId", chatId)
            .param("linkId", linkId)
            .query(Boolean.class)
            .single();
    }

    @Override
    public void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash) {
        jdbcClient.sql("UPDATE link SET last_check_time = :lastCheckTime, hash_int = :hash WHERE id = :linkId")
            .param("lastCheckTime", lastCheckTime)
            .param("linkId", linkId)
            .param("hash", hash)
            .update();
    }
}
