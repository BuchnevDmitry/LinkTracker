package edu.java.model;

public record QuestionRequest(
    String number,
    String order,
    String sort,
    String site
) {
}
