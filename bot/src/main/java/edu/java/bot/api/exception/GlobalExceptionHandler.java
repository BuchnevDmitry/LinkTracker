package edu.java.bot.api.exception;

import edu.java.bot.api.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleRuntimeErrors(HttpMessageNotReadableException ex) {
        List<String> stacktrace = Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            "Некорректные параметры запроса",
            "BAD_REQUEST",
            ex.getClass().getName(),
            ex.getMessage(),
            stacktrace
        );
        return apiErrorResponse;
    }
}
