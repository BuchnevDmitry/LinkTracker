package edu.java.scrapper.configuration;

import edu.java.scrapper.configuration.access.AccessType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = true)
@EnableCaching
public record ApplicationConfig(
    @NotEmpty
    String gitHubUri,
    @NotEmpty
    String stackOverflowUri,

    @NotEmpty
    String gitHubToken,

    @NotEmpty
    String botUri,

    @NotNull
    Boolean useQueue,

    @NotNull
    AccessType databaseAccessType
) {
}
