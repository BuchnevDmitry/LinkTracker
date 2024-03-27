package edu.java.scrapper.configuration.access;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public ChatRepository chatRepository(JdbcClient jdbcClient) {
        return new JdbcChatRepository(jdbcClient);
    }

    @Bean
    public LinkRepository linkRepository(JdbcClient jdbcClient) {
        return new JdbcLinkRepository(jdbcClient);
    }
}
