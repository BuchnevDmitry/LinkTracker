package edu.java.scrapper.domain;

import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class JdbcChatRepository {
    private final JdbcClient jdbcClient;

    public JdbcChatRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<ChatResponse> findAll() {
        return jdbcClient.sql("SELECT id, created_at, created_by FROM chat")
            .query(ChatResponse.class)
            .list();
    }

    public void add(AddChatRequest chat) {
        jdbcClient.sql("INSERT INTO chat(id, created_at, created_by) values(?,?,?)")
            .params(List.of(chat.id(), OffsetDateTime.now(), chat.createdBy()))
            .update();
    }

    public void remove(Long id) {
        jdbcClient.sql("delete from chat where id = :id")
            .param("id", id)
            .update();
    }

    public boolean exist(Long chatId) {
        return jdbcClient.sql("SELECT EXISTS(SELECT id FROM chat WHERE id = :chatId)")
            .param("chatId", chatId)
            .query(Boolean.class)
            .single();
    }
}
