package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.link", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String gitHubUri,
    @NotEmpty
    String stackOverflowUri

) {
}
