package edu.java.scrapper.api.exception.handler;

import edu.java.scrapper.api.controller.LinkController;
import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.model.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = LinkController.class)
public class LinkExceptionHandler {
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Ссылка уже добавлена")
    })
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(BadRequestException ex) {
        return ex.toApiErrorResponse();
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "Ссылка не найдена")
    })
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
        return ex.toApiErrorResponse();
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "409",
            description = "Ссылка уже добавлена")
    })
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return ex.toApiErrorResponse();
    }

}
