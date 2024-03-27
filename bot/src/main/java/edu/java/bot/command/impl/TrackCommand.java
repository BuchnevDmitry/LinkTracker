package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.exception.ParseException;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.model.request.AddLinkRequest;
import edu.java.bot.model.response.LinkResponse;
import edu.java.bot.util.ParserUtil;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.BotMessages.LINK_MISSING;
import static edu.java.bot.util.BotMessages.LINK_WRONG_FORMAT;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("Запрос в базу об информации по данной ссылке");
        try {
            String url = ParserUtil.parseUrl(update.message().text());
            log.info("Вот такой url " + url);
            LinkResponse link = scrapperClient.addLink(
                update.message().chat().id(),
                new AddLinkRequest(new URI(url), update.message().from().username())
            );
            String stringLog = String.format("Отслеживание id: %d url: %s", link.id(), link.url());
            log.info(stringLog);
            return new SendMessage(update.message().chat().id(), stringLog);
        } catch (ParseException e) {
            return new SendMessage(update.message().chat().id(), LINK_MISSING);
        } catch (URISyntaxException e) {
            return new SendMessage(update.message().chat().id(), LINK_WRONG_FORMAT);
        }
    }
}
