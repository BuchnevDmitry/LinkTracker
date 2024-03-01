package edu.java.scrapper.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record QuestionResponse(List<ItemResponse> items) {
    public record ItemResponse(
        @JsonProperty("question_id") long id,
        @JsonProperty("is_answered") boolean isAnswered,
        @JsonProperty("view_count") long viewCount,
        @JsonProperty("answer_count") long answerCount,
        long score,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
        @JsonProperty("creation_date") OffsetDateTime creationDate,
        String title
        ) {}
}
