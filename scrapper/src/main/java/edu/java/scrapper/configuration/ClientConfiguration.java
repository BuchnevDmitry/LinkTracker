package edu.java.scrapper.configuration;

import edu.java.scrapper.service.GitHubClient;
import edu.java.scrapper.service.StackOverflowClient;
import edu.java.scrapper.service.impl.GitHubClientImpl;
import edu.java.scrapper.service.impl.StackOverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient getGitHubClient() {
        return new GitHubClientImpl(WebClient.builder(), "https://api.github.com/repos/");
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClientImpl(WebClient.builder(), "https://api.stackexchange.com/2.3/questions/");
    }
}
