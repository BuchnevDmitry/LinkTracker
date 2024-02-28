package edu.java.scrapper.api.model;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
