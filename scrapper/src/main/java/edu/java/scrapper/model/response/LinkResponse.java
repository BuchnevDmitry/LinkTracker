package edu.java.scrapper.model.response;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
