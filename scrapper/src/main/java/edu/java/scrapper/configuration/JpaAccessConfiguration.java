package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jooq.impl.JooqChatRepository;
import edu.java.scrapper.domain.jooq.impl.JooqLinkRepository;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JpaAccessConfiguration {

    public ChatRepository chatRepository(JpaChatRepository jpaChatRepository) {
        return jpaChatRepository;
    }

    public LinkRepository linkRepository(JpaLinkRepository jpaLinkRepository) {
        return jpaLinkRepository;
    }
}
