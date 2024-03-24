package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcChatRepository implements ChatRepository {
    private final JdbcClient jdbcClient;

    public JdbcChatRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Chat> findAll() {
        return jdbcClient.sql("SELECT id, created_at, created_by FROM chat")
            .query(Chat.class)
            .list();
    }

    @Override
    @Transactional
    public void add(Long id, AddChatRequest chat) {
        jdbcClient.sql("""
                INSERT INTO chat(id, created_at, created_by)
                VALUES (:id, :createdAt, :createdBy)
                """)
            .param("id", id)
            .param("createdAt", OffsetDateTime.now())
            .param("createdBy", chat.createdBy())
            .update();
    }

    @Override
    @Transactional
    public void remove(Long id) {
        jdbcClient.sql("delete from chat where id = :id")
            .param("id", id)
            .update();
    }

    @Override
    public boolean exists(Long chatId) {
        return jdbcClient.sql("SELECT EXISTS(SELECT id FROM chat WHERE id = :chatId)")
            .param("chatId", chatId)
            .query(Boolean.class)
            .single();
    }
}
