package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.model.request.RepositoryRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParseServiceTest {

    private ParseService parseService = new ParseService();


    @Test
    public void shouldErrorWhenGitHubUrlIsNotValid() {
        String url = "https://github.com/username/";

        Assertions.assertThrows(BadRequestException.class, () -> parseService.parseUrlToRepositoryRequest(url));
    }

    @Test
    public void shouldErrorWhenStackOverflowUrlIsNotValid() {
        String url = "https://stackoverflow.com/questions/";

        Assertions.assertThrows(BadRequestException.class, () -> parseService.parseUrlToQuestionRequest(url));
    }

    @Test
    public void shouldRequestWhenStackOverflowUrlIsValid() {
        String url = "https://stackoverflow.com/questions/123121232/name";
        QuestionRequest validRequest = new QuestionRequest("123121232");


        QuestionRequest questionRequest = parseService.parseUrlToQuestionRequest(url);

        Assertions.assertEquals(questionRequest, validRequest);
    }

    @Test
    public void shouldRequestWhenGitHubUrlIsValid() {
        String url = "https://github.com/username/repName";
        RepositoryRequest validRequest = new RepositoryRequest("username", "repName");


        RepositoryRequest repositoryRequest = parseService.parseUrlToRepositoryRequest(url);

        Assertions.assertEquals(repositoryRequest, validRequest);
    }

}
