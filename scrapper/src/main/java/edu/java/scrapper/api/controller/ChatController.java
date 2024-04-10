package edu.java.scrapper.api.controller;

import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.service.ChatService;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class ChatController {
    private final ChatService chatService;
    private final Counter messageCounter;

    public ChatController(
        ChatService chatService,
        Counter messageCounter
    ) {
        this.chatService = chatService;
        this.messageCounter = messageCounter;
    }

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат зарегистрирован")
    })
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void registerChat(
        @PathVariable Long id,
        @RequestBody @Valid AddChatRequest request
    ) {
        messageCounter.increment();
        chatService.register(id, request);
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат успешно удалён")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteChat(@PathVariable @NotNull Long id) {
        messageCounter.increment();
        chatService.unregister(id);
    }
}
