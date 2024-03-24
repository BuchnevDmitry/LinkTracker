package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jooq.impl.JooqChatRepository;
import edu.java.scrapper.domain.jooq.impl.JooqLinkRepository;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    public ChatRepository chatRepository(DSLContext dslContext) {
        return new JooqChatRepository(dslContext);
    }

    public LinkRepository linkRepository(DSLContext dslContext) {
        return new JooqLinkRepository(dslContext);
    }
}
