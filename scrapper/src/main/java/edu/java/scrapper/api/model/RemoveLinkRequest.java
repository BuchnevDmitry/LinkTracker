package edu.java.scrapper.api.model;

import java.net.URI;

public record RemoveLinkRequest(
    URI link
) {
}
