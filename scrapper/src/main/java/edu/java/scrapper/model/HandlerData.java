package edu.java.scrapper.model;

public record HandlerData(
    Integer hash,
    UpdateStatus typeUpdate,
    String description
) {
}
