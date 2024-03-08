package edu.java.scrapper.api.exception.handler;

import edu.java.scrapper.api.controller.ChatController;
import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.model.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ChatController.class)
public class ChatExceptionHandler {

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует")
    })
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
        return ex.toApiErrorResponse();
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "409",
            description = "Чат уже существует")
    })
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return ex.toApiErrorResponse();
    }

}
