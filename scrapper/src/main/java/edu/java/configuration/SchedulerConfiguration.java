package edu.java.configuration;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {
    @Bean
    public Duration schedulerInterval(ApplicationConfig applicationConfig) {
        return applicationConfig.scheduler().interval();
    }

}