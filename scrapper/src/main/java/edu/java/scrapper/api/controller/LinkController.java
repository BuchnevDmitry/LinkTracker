package edu.java.scrapper.api.controller;

import edu.java.scrapper.api.model.AddLinkRequest;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.model.ListLinksResponse;
import edu.java.scrapper.api.model.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinkController {

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылки успешно получены")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        return null;
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно добавлена")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest request
    ) {
        return null;
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно убрана")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    LinkResponse deleteLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest request
    ) {
        return null;
    }

}
