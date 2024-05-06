package edu.java.scrapper.configuration;

import edu.java.scrapper.kafka.ScrapperQueueProducer;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.service.Updater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class UpdaterQueueConfiguration {
    @Bean
    public Updater updater(KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate) {
        return new ScrapperQueueProducer(kafkaTemplate);
    }
}
