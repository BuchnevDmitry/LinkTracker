package edu.java.scrapper.domain.model;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link(
    Long id,
    URI url,
    OffsetDateTime lastCheckTime,
    OffsetDateTime createdAt,
    String createdBy,
    Integer hashInt
) {

}
