package edu.java.scrapper.api.exception;

import edu.java.scrapper.model.response.ApiErrorResponse;
import edu.java.scrapper.service.ApiErrorResponseBuilder;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ApiErrorResponseBuilder apiErrorResponseBuilder;

    public GlobalExceptionHandler(ApiErrorResponseBuilder apiErrorResponseBuilder) {
        this.apiErrorResponseBuilder = apiErrorResponseBuilder;
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса")
    })
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(RuntimeException ex) {
        return apiErrorResponseBuilder.buildErrorResponse(HttpStatus.BAD_REQUEST, "Некорректные параметры запроса", ex);
    }
}
