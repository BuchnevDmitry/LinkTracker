package edu.java.scrapper.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record RepositoryEventResponse(
    Long id,
    String type,
    Payload payload,
    @JsonProperty("created_at")
    OffsetDateTime createdAt
) {
    public record Payload(
        String action,
        @JsonProperty("ref_type")
        String refType,
        List<Commit> commits

    ) {
        public record Commit(
            String message
        ) {
        }
    }
}


