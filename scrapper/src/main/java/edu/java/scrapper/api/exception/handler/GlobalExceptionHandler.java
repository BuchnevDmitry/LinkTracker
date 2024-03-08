package edu.java.scrapper.api.exception.handler;

import edu.java.scrapper.model.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса")
    })
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(RuntimeException ex) {
        List<String> stacktrace = Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();
        return new ApiErrorResponse(
            "Некорректные параметры запроса",
            "400",
            ex.getClass().getName(),
            ex.getMessage(),
            stacktrace
        );
    }
}
