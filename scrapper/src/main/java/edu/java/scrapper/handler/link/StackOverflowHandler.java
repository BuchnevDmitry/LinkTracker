package edu.java.scrapper.handler.link;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.UpdateStatus;
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
            questionRequest.setSort("creation");
            AnswerResponse answerResponse = stackOverflowClient.fetchQuestionAnswer(questionRequest);
            HandlerData reusltHandlerData =
                new HandlerData(questionResponse.hashCode(), UpdateStatus.NOT_UPDATE, "обновлений нeт");
            if (linkRepository.exists(url)) {
                Link link = linkRepository.findByUrl(url).get();
                reusltHandlerData = findUpdates(link, questionRequest, answerResponse, questionResponse);
            }
            return reusltHandlerData;
        }
        return super.handle(url);
    }

    public HandlerData findUpdates(Link link,
        QuestionRequest questionRequest,
        AnswerResponse answerResponse,
        QuestionResponse questionResponse
    ) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("События:\n");
        boolean updateExist = false;
        if (!link.hashInt().equals(questionRequest.hashCode())) {
            for (AnswerResponse.ItemResponse item : answerResponse.items()) {
                if (link.lastCheckTime().isBefore(item.creationDate())) {
                    updateExist = true;
                    stringBuilder.append("появился новый ответ\n");
                }
            }
            if (updateExist) {
               return new HandlerData(
                    questionResponse.hashCode(),
                    UpdateStatus.UPDATE,
                    stringBuilder.toString()
                );
            }
        }
        return new HandlerData(questionResponse.hashCode(), UpdateStatus.NOT_UPDATE, "обновлений нет");
    }
}
