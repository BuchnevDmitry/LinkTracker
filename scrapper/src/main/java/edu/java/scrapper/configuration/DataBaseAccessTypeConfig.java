package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = true)
public record DataBaseAccessTypeConfig(
    @NotNull
    AccessType databaseAccessType
) {

}
