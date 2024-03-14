package edu.java.scrapper.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record AddLinkRequest(
    @NotNull
    URI url,
    @NotEmpty
    String createdBy
) {
}
