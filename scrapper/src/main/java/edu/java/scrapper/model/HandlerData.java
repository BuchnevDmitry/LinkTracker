package edu.java.scrapper.model;

public record HandlerData(
    Integer hash,
    LinkStatus typeUpdate,
    String description
) {
}
