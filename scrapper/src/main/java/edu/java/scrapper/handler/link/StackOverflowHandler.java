package edu.java.scrapper.handler.link;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.LinkStatus;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.model.response.AnswerResponse;
import edu.java.scrapper.model.response.QuestionResponse;
import edu.java.scrapper.service.ParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StackOverflowHandler extends HandlerLink {
    private final StackOverflowClient stackOverflowClient;
    private final ParseService parseService;

    private final LinkRepository linkRepository;

    public StackOverflowHandler(
        StackOverflowClient stackOverflowClient, ParseService parseService,
        @Qualifier("jdbcLinkRepository") LinkRepository linkRepository
    ) {
        this.stackOverflowClient = stackOverflowClient;
        this.parseService = parseService;
        this.linkRepository = linkRepository;
    }

    @Override
    public HandlerData handle(String url) {
        if (url.startsWith("https://stackoverflow.com/")) {
            QuestionRequest questionRequest = parseService.parseUrlToQuestionRequest(url);
            QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(questionRequest);
            return handleUpdates(url, questionRequest, questionResponse);
        }
        return super.handle(url);
    }

    private HandlerData handleUpdates(
        String url,
        QuestionRequest questionRequest,
        QuestionResponse questionResponse
    ) {
        if (linkRepository.exists(url)) {
            questionRequest.setSort("creation");
            Link link = linkRepository.findByUrl(url).get();
            if (!link.hashInt().equals(questionResponse.hashCode())) {
                AnswerResponse answerResponse = stackOverflowClient.fetchQuestionAnswer(questionRequest);
                String updateMessage = handleMessageUpdates(answerResponse, link);
                if (!updateMessage.isEmpty()) {
                    return new HandlerData(
                        questionResponse.hashCode(),
                        LinkStatus.UPDATE,
                        updateMessage
                    );
                }
            }
            return new HandlerData(
                questionResponse.hashCode(),
                LinkStatus.NOT_UPDATE,
                "обновлений нет"
            );
        }
        return new HandlerData(
            questionResponse.hashCode(),
            LinkStatus.NOT_EXIST,
            "ссылки не существует"
        );
    }

    private String handleMessageUpdates(AnswerResponse answerResponse, Link link) {
        StringBuilder updateMessageBuilder = new StringBuilder();
        for (AnswerResponse.ItemResponse item : answerResponse.items()) {
            if (link.lastCheckTime().isBefore(item.creationDate())) {
                updateMessageBuilder.append("появился новый ответ \uD83D\uDD14 \n");
            }
        }
        return updateMessageBuilder.toString();
    }
}
