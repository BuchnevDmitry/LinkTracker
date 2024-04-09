package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.RetryPolicy;
import edu.java.scrapper.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class ClientConfiguration {

    @Bean
    public GitHubClient gitHubClient(ApplicationConfig applicationConfig, RetryPolicy retryPolicy) {
        return new GitHubClient(
            WebClient.builder(),
            applicationConfig.gitHubUri(),
            applicationConfig.gitHubToken(),
            retryPolicy
        );
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig applicationConfig, RetryPolicy retryPolicy) {
        return new StackOverflowClient(WebClient.builder(), applicationConfig.stackOverflowUri(), retryPolicy);
    }

}
