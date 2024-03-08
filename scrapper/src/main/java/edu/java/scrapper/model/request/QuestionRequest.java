package edu.java.scrapper.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequest {
    private String number;
    private String order;
    private String sort;
    private String site;

    public QuestionRequest(String number) {
        this(number, "desc", "activity", "stackoverflow");
    }

    public QuestionRequest(String number, String order, String sort, String site) {
        this.number = number;
        this.order = order;
        this.sort = sort;
        this.site = site;
    }
}
