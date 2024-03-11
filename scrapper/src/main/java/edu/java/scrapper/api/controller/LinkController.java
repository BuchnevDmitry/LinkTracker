package edu.java.scrapper.api.controller;

import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import edu.java.scrapper.model.response.LinkResponse;
import edu.java.scrapper.model.response.ListLinksResponse;
import edu.java.scrapper.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылки успешно получены")
    })
    @GetMapping("/{tgChatId}")
    @ResponseStatus(HttpStatus.OK)
    ListLinksResponse getLinks(@PathVariable Long tgChatId) {
        return linkService.getLinks(tgChatId);
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно добавлена")
    })
    @PostMapping("/{tgChatId}")
    @ResponseStatus(HttpStatus.OK)
    LinkResponse addLink(
        @PathVariable Long tgChatId,
        @RequestBody @Valid AddLinkRequest request
    ) {
        return linkService.addLink(tgChatId, request.link());
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно убрана")
    })
    @DeleteMapping("/{tgChatId}")
    @ResponseStatus(HttpStatus.OK)
    LinkResponse deleteLink(
        @PathVariable Long tgChatId,
        @RequestBody @Valid RemoveLinkRequest request
    ) {
        return linkService.deleteLink(tgChatId, request.link());
    }

}
