package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.RetryPolicy;
import edu.java.scrapper.service.Updater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class UpdateClientConfiguration {
    @Bean
    public Updater updater(ApplicationConfig applicationConfig, RetryPolicy retryPolicy) {
        return new BotClient(WebClient.builder(), applicationConfig.botUri(), retryPolicy);
    }
}
