package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    long id,
    @JsonProperty("full_name") String fullName,
    String description,
    @JsonProperty("created_at") OffsetDateTime created,
    @JsonProperty("updated_at") OffsetDateTime updated,
    @JsonProperty("pushed_at") OffsetDateTime pushed,
    @JsonProperty("stargazers_count") long stargazersCount,
    @JsonProperty("watchers") long watchers,
    String language,
    @JsonProperty("forks") long forks
) {

}
