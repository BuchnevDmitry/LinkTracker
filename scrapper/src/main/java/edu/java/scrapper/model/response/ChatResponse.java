package edu.java.scrapper.model.response;

import java.time.OffsetDateTime;

public record ChatResponse(
    Long id,
    OffsetDateTime createdAt,
    String createdBy
) {
}
