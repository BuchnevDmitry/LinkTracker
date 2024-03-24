package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    public ChatRepository chatRepository(JdbcClient jdbcClient) {
        return new JdbcChatRepository(jdbcClient);
    }

    public LinkRepository linkRepository(JdbcClient jdbcClient) {
        return new JdbcLinkRepository(jdbcClient);
    }
}
