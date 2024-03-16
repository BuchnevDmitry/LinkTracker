package edu.java.scrapper.api.controller;

import edu.java.scrapper.api.mapper.LinkMapper;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import edu.java.scrapper.model.response.LinkResponse;
import edu.java.scrapper.model.response.ListLinksResponse;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
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

    private final JdbcLinkService linkService;

    private final LinkMapper linkMapper;

    public LinkController(JdbcLinkService linkService, LinkMapper linkMapper) {
        this.linkService = linkService;
        this.linkMapper = linkMapper;
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
        List<LinkResponse> linkResponses = linkMapper.mapToDto(linkService.getLinks(tgChatId));
        return new ListLinksResponse(linkResponses, linkResponses.size());
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
        return linkMapper.mapToDto(linkService.addLink(tgChatId, request));
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
        return linkMapper.mapToDto(linkService.deleteLink(tgChatId, request));
    }

}
