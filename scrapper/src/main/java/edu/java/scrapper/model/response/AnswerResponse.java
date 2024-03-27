package edu.java.scrapper.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record AnswerResponse(List<ItemResponse> items) {
    public record ItemResponse(
        @JsonProperty("creation_date") OffsetDateTime creationDate
    ) {
    }
}
