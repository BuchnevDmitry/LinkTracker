package edu.java.scrapper.model.request;

public record QuestionRequest(
    String number,
    String order,
    String sort,
    String site
) {
}
