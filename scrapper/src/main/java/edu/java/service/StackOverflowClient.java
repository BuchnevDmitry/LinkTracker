package edu.java.service;

import edu.java.model.QuestionRequest;
import edu.java.model.QuestionResponse;

public interface StackOverflowClient {
    QuestionResponse fetchQuestion(QuestionRequest request);
}
