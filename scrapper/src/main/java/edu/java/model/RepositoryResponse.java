package edu.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    String id,
    String node_id,
    String name,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("private") boolean isPrivate,
    String description,
    boolean fork,
    @JsonProperty("created_at") OffsetDateTime created,
    @JsonProperty("updated_at") OffsetDateTime updated,
    @JsonProperty("pushed_at") OffsetDateTime pushed,
    long size,
    @JsonProperty("stargazers_count") long stargazersCount,
    @JsonProperty("watchers") long watchers,
    String language,
    @JsonProperty("forks") long forks,
    String visibility

) {

}
