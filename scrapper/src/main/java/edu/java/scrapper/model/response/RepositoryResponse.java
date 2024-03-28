package edu.java.scrapper.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    long id,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("created_at") OffsetDateTime created,
    @JsonProperty("updated_at") OffsetDateTime updated,
    @JsonProperty("pushed_at") OffsetDateTime pushed,
    @JsonProperty("open_issues_count") Long issuesCount,
    @JsonProperty("stargazers_count") long stargazersCount
) {

}
