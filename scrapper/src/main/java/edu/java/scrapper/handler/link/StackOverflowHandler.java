package edu.java.scrapper.handler.link;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.service.ParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StackOverflowHandler extends HandlerLink {
    private final StackOverflowClient stackOverflowClient;
    private final ParseService parseService;

    public StackOverflowHandler(StackOverflowClient stackOverflowClient, ParseService parseService) {
        this.stackOverflowClient = stackOverflowClient;
        this.parseService = parseService;
    }

    @Override
    public boolean handle(String url) {
        if (url.startsWith("https://stackoverflow.com/")) {
            QuestionRequest questionRequest = parseService.parseUrlToQuestionRequest(url);
            stackOverflowClient.fetchQuestion(questionRequest);
            return true;
        }
        return super.handle(url);
    }
}
