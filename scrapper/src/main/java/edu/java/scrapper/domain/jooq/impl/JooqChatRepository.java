package edu.java.scrapper.domain.jooq.impl;

import edu.java.scrapper.domain.ChatRepository;
//import edu.java.scrapper.domain.jooq.tables.Chat;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.model.request.AddChatRequest;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {

    private final DSLContext dslContext;

    @Override
    public List<Chat> findAll() {
        return dslContext.select(CHAT.ID, CHAT.CREATED_AT, CHAT.CREATED_BY)
            .from(CHAT)
            .fetchInto(Chat.class);
    }

    @Override
    public void add(Long id, AddChatRequest chatRequest) {
        dslContext.insertInto(CHAT)
            .set(CHAT.ID, id)
            .set(CHAT.CREATED_AT, OffsetDateTime.now())
            .set(CHAT.CREATED_BY, chatRequest.createdBy())
            .execute();
    }

    @Override
    public void remove(Long id) {
        dslContext.delete(CHAT)
            .where(CHAT.ID.equal(id))
            .execute();
    }

    @Override
    public boolean exists(Long chatId) {
        return dslContext.selectCount()
            .from(CHAT)
            .where(CHAT.ID.eq(chatId))
            .fetchOne(0, int.class) > 0;
    }
}
