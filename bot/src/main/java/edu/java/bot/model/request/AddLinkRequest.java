package edu.java.bot.model.request;

import java.net.URI;

public record AddLinkRequest(
    URI url,
    String createdBy
) {
}
