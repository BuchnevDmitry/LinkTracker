package edu.java.scrapper.service;

import edu.java.scrapper.model.QuestionRequest;
import edu.java.scrapper.model.QuestionResponse;

public interface StackOverflowClient {
    QuestionResponse fetchQuestion(QuestionRequest request);
}
