package edu.java.scrapper.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddChatRequest(
    @NotBlank
    String createdBy
) {
}
