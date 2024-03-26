package edu.java.scrapper.domain.jooq.impl;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.jooq.tables.Chat.CHAT;
import static edu.java.scrapper.domain.jooq.tables.ChatLink.CHAT_LINK;
import static edu.java.scrapper.domain.jooq.tables.Link.LINK;

@Repository
@Transactional
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Autowired
    public JooqLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Link> findAllByLastCheckTimeBefore(OffsetDateTime time) {
        return dslContext.selectFrom(LINK)
            .where(LINK.LAST_CHECK_TIME.lessThan(time))
            .fetchInto(Link.class);
    }

    @Override
    public void add(AddLinkRequest link, Integer hash) {
        dslContext.insertInto(LINK)
            .set(LINK.URL, link.url().toString())
            .set(LINK.LAST_CHECK_TIME, OffsetDateTime.now())
            .set(LINK.CREATED_AT, OffsetDateTime.now())
            .set(LINK.CREATED_BY, link.createdBy())
            .set(LINK.HASH_INT, (long) hash)
            .execute();
    }

    @Override
    public void remove(Long linkId) {
        dslContext.deleteFrom(LINK)
            .where(LINK.ID.eq(linkId))
            .execute();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return dslContext.selectFrom(LINK)
            .where(LINK.URL.eq(url))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public boolean exists(String url) {
        return dslContext.selectCount()
            .from(LINK)
            .where(LINK.URL.eq(url))
            .fetchOneInto(int.class) > 0;
    }

    @Override
    public boolean existsLinkToChatByLinkId(Long linkId) {
        return dslContext.selectCount()
            .from(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(linkId))
            .fetchOneInto(int.class) > 0;
    }

    @Override
    public void addLinkToChat(Long chatId, Long linkId) {
        dslContext.insertInto(CHAT_LINK)
            .set(CHAT_LINK.CHAT_ID, chatId)
            .set(CHAT_LINK.LINK_ID, linkId)
            .execute();
    }

    @Override
    public void removeLinkToChat(Long chatId, Long linkId) {
        dslContext.deleteFrom(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(linkId)))
            .execute();
    }

    @Override
    public List<Link> findLinks(Long chatId) {
        return dslContext.select(LINK.fields())
            .from(CHAT_LINK)
            .join(LINK).on(CHAT_LINK.LINK_ID.eq(LINK.ID))
            .where(CHAT_LINK.CHAT_ID.eq(chatId))
            .fetchInto(Link.class);
    }

    @Override
    public List<ChatResponse> findChats(Long linkId) {
        return dslContext.select(CHAT.fields())
            .from(CHAT_LINK)
            .join(CHAT).on(CHAT_LINK.CHAT_ID.eq(CHAT.ID))
            .where(CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(ChatResponse.class);
    }

    @Override
    public boolean existsLinkToChat(Long chatId, Long linkId) {
        return dslContext.selectCount()
            .from(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(linkId)))
            .fetchOneInto(int.class) > 0;
    }

    @Override
    public void updateLink(Long linkId, OffsetDateTime lastCheckTime, Integer hash) {
        dslContext.update(LINK)
            .set(LINK.LAST_CHECK_TIME, lastCheckTime)
            .set(LINK.HASH_INT, (long) hash)
            .where(LINK.ID.eq(linkId))
            .execute();
    }

}
