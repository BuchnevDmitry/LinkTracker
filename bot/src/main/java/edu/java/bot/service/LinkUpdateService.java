package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LinkUpdateService {

    private final TelegramBot telegramBot;

    public void update(LinkUpdateRequest request) {
        List<Long> chats = request.tgChatIds();
        for (Long chatId : chats) {
            telegramBot.execute(new SendMessage(
                chatId,
                String.format("По url : %s\nОбновления:\n%s", request.url(), request.description())
            ));
        }
    }
}
