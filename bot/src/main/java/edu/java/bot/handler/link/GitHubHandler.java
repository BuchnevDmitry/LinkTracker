package edu.java.bot.handler.link;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.util.ParserUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GitHubHandler extends HandlerLink {

    @Override
    public SendMessage handle(Update update) {
        String url = ParserUtil.parseUrl(update.message().text());
        log.info("Вот такой url " + url);
        if (url.startsWith("https://github.com/")) {
            //TODO: обработка логики для https://github.com/
            String stringLog = String.format("Отслеживание %s", url);
            log.info(stringLog);
            return new SendMessage(update.message().chat().id(), stringLog);
        }
        return super.handle(update);
    }
}