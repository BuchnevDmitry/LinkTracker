package edu.java.scrapper.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageCounterConfiguration {
    @Bean
    public Counter messagesProcessedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("messages_processed_counter")
            .description("Number of processed messages")
            .tags("application", "scrapper")
            .register(meterRegistry);
    }
}
