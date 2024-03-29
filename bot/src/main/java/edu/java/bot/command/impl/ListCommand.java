package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.model.response.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.BotMessages.TRACK_LINKS_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Cписок отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("Запрос в базу данных о наличии ссылок!");
        ListLinksResponse listLinksResponse = scrapperClient.getLinks(update.message().chat().id());
        if (!listLinksResponse.links().isEmpty()) {
            StringBuilder result = new StringBuilder();
            listLinksResponse.links()
                .forEach(l -> result.append(l.id())
                    .append(" - ")
                    .append(l.url())
                    .append("\n")
                );
            return new SendMessage(update.message().chat().id(), result.toString()).parseMode(ParseMode.HTML);
        }
        return new SendMessage(update.message().chat().id(), TRACK_LINKS_NOT_FOUND).parseMode(ParseMode.HTML);
    }
}
