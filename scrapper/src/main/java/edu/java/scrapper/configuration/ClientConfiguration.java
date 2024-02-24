package edu.java.scrapper.configuration;

import edu.java.scrapper.service.GitHubClientImpl;
import edu.java.scrapper.service.StackOverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClientImpl gitHubClient(ApplicationConfig applicationConfig) {
        return new GitHubClientImpl(WebClient.builder(), applicationConfig.gitHub());
    }

    @Bean
    public StackOverflowClientImpl stackOverflowClient(ApplicationConfig applicationConfig) {
        return new StackOverflowClientImpl(WebClient.builder(), applicationConfig.stackOverflow());
    }
}
