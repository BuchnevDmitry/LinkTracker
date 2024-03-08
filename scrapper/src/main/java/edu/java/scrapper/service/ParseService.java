package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.model.request.RepositoryRequest;
import org.springframework.stereotype.Service;


@Service
@SuppressWarnings("MagicNumber")
public class ParseService {


    public RepositoryRequest parseUrlToRepositoryRequest(String url) {
        try {
            String[] urlParts = url.split("/");
            return new RepositoryRequest(urlParts[3], urlParts[4]);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new BadRequestException("Некорретная ссылка ресурса github");
        }
    }

    public QuestionRequest parseUrlToQuestionRequest(String url) {
        try {
            String[] urlParts = url.split("/");
            return new QuestionRequest(urlParts[4]);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new BadRequestException("Некорректная ссылка ресурса stackoverflow");
        }
    }
}
