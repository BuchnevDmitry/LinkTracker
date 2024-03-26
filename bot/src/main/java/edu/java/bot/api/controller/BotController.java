package edu.java.bot.api.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.request.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class BotController {

    private final TelegramBot telegramBot;

    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Обновление обработано")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    void addUpdate(@RequestBody @Valid LinkUpdateRequest request) {
        List<Long> chats = request.tgChatIds();
        for (Long chatId : chats) {
            telegramBot.execute(new SendMessage(
                chatId,
                String.format("По url : %s %s", request.url(), request.description())
            ));
        }
    }
}
