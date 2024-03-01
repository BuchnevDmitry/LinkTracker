package edu.java.scrapper.api.exception;

import edu.java.scrapper.api.controller.LinkController;
import edu.java.scrapper.model.response.ApiErrorResponse;
import edu.java.scrapper.service.ApiErrorResponseBuilder;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = LinkController.class)
public class LinkExceptionHandler {
    private final ApiErrorResponseBuilder apiErrorResponseBuilder;

    public LinkExceptionHandler(ApiErrorResponseBuilder apiErrorResponseBuilder) {
        this.apiErrorResponseBuilder = apiErrorResponseBuilder;
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "Ссылка не найдена")
    })
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
        return apiErrorResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "409",
            description = "Ссылка уже добавлена")
    })
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleAlreadyExistsException(AlreadyExistsException ex) {
        return apiErrorResponseBuilder.buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }
}
