package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient gitHubClient(ApplicationConfig applicationConfig) {
        return new GitHubClient(WebClient.builder(), applicationConfig.gitHubUri());
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig applicationConfig) {
        return new StackOverflowClient(WebClient.builder(), applicationConfig.stackOverflowUri());
    }
}
