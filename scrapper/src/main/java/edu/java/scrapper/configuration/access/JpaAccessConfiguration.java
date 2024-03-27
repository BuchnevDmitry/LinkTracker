package edu.java.scrapper.configuration.access;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public ChatRepository chatRepository(JpaChatRepository jpaChatRepository) {
        return jpaChatRepository;
    }

    @Bean
    public LinkRepository linkRepository(JpaLinkRepository jpaLinkRepository) {
        return jpaLinkRepository;
    }
}
