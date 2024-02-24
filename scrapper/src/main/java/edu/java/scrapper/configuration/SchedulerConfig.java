package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.scheduler", ignoreUnknownFields = false)
public record SchedulerConfig(
    boolean enable,
    @NotNull
    Duration interval,
    @NotNull
    Duration forceCheckDelay) {
}
