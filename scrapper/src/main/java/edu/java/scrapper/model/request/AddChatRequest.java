package edu.java.scrapper.model.request;

import jakarta.validation.constraints.NotBlank;

public record AddChatRequest(
    @NotBlank
    String createdBy
) {
}
