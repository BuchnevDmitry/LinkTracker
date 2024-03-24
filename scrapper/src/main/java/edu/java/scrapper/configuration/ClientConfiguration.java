package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.StackOverflowClient;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class ClientConfiguration {

    @Bean
    public GitHubClient gitHubClient(ApplicationConfig applicationConfig) {
        return new GitHubClient(WebClient.builder(), applicationConfig.gitHubUri(), applicationConfig.gitHubToken());
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig applicationConfig) {
        return new StackOverflowClient(WebClient.builder(), applicationConfig.stackOverflowUri());
    }

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

}
