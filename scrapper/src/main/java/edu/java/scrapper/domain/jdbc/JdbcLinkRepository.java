package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcClient jdbcClient;

    private static final String URL = "url";

    private static final String LAST_CHECK_TIME = "lastCheckTime";

    private static final String HASH = "hash";

    private static final String LINK_ID = "linkId";

    private static final String CHAT_ID = "chatId";

    public JdbcLinkRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Link> findAllByLastCheckTimeBefore(OffsetDateTime time) {
        return jdbcClient.sql("""
                SELECT id, url, last_check_time, created_at, created_by, hash_int
                FROM link
                WHERE last_check_time < :time
                """)
            .param("time", time)
            .query(Link.class)
            .list();
    }

    @Override
    @Transactional
    public void add(AddLinkRequest link, Integer hash) {
        jdbcClient.sql("""
                INSERT INTO link(url, last_check_time, created_at, created_by, hash_int)
                VALUES(:url, :lastCheckTime, :createdAt, :createdBy, :hash)
                """)
            .param(URL, link.url().toString())
            .param(LAST_CHECK_TIME, OffsetDateTime.now())
            .param("createdAt", OffsetDateTime.now())
            .param("createdBy", link.createdBy())
            .param(HASH, hash)
            .update();
    }

    @Override
    @Transactional
    public void remove(Long linkId) {
        jdbcClient.sql("DELETE FROM link where id = :id")
            .param("id", linkId)
            .update();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return jdbcClient.sql("""
                SELECT id, url, last_check_time, created_at, created_by, hash_int
                FROM link where url = :url
                """)
            .param(URL, url)
            .query(Link.class)
            .optional();
    }

    @Override
    public boolean exists(String url) {
        return jdbcClient.sql("SELECT EXISTS(SELECT id FROM link WHERE url = :url)")
            .param(URL, url)
            .query(Boolean.class)
            .single();
    }

    @Override
    public boolean existsLinkToChatByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM chat_link WHERE link_id = :linkId)")
            .param(LINK_ID, linkId)
            .query(Boolean.class)
            .single();
    }

    @Override
    @Transactional
    public void addLinkToChat(Long chatId, Long linkId) {
        jdbcClient.sql("""
                INSERT INTO chat_link(chat_id, link_id)
                VALUES(:chatId, :linkId)
                """)
            .param(CHAT_ID, chatId)
            .param(LINK_ID, linkId)
            .update();
    }

    @Override
    @Transactional
    public void removeLinkToChat(Long chatId, Long linkId) {
        jdbcClient.sql("DELETE FROM chat_link where chat_id = :chatId and link_id = :linkId")
            .param(CHAT_ID, chatId)
            .param(LINK_ID, linkId)
            .update();
    }

    @Override
    public List<Link> findLinks(Long chatId) {
        return jdbcClient.sql("""
                SELECT id, url, last_check_time, created_at, created_by, hash_int
                FROM chat_link
                JOIN link ON chat_link.link_id = link.id
                WHERE chat_link.chat_id = :chatId
                """)
            .param(CHAT_ID, chatId)
            .query(Link.class)
            .list();
    }

    @Override
    public List<Chat> findChats(Long linkId) {
        return jdbcClient.sql("""
                SELECT id, created_at, created_by
                FROM chat_link
                JOIN chat ON chat_link.chat_id = chat.id
                WHERE chat_link.link_id = :linkId
                """)
            .param(LINK_ID, linkId)
            .query(Chat.class)
            .list();
    }

    @Override
    public boolean existsLinkToChat(Long chatId, Long linkId) {
        return jdbcClient.sql("""
                SELECT EXISTS(
                    SELECT 1 FROM chat_link
                    WHERE chat_id = :chatId and link_id = :linkId
                )
                """)
            .param(CHAT_ID, chatId)
            .param(LINK_ID, linkId)
            .query(Boolean.class)
            .single();
    }

    @Override
    @Transactional
    public void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash) {
        jdbcClient.sql("""
                UPDATE link SET last_check_time = :lastCheckTime, hash_int = :hash
                WHERE id = :linkId
                """)
            .param(LAST_CHECK_TIME, lastCheckTime)
            .param(LINK_ID, linkId)
            .param(HASH, hash)
            .update();
    }
}
