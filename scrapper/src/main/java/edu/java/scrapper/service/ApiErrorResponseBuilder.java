package edu.java.scrapper.service;

import edu.java.scrapper.model.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ApiErrorResponseBuilder {
    public ApiErrorResponse buildErrorResponse(HttpStatus status, String defaultMessage, RuntimeException ex) {
        List<String> stacktrace = Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();

        return new ApiErrorResponse(
            defaultMessage,
            status.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            stacktrace
        );
    }
}
