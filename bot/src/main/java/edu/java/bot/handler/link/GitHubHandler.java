package edu.java.bot.handler.link;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.request.AddLinkRequest;
import edu.java.bot.model.response.LinkResponse;
import edu.java.bot.util.ParserUtil;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GitHubHandler extends HandlerLink {

    @Override
    public SendMessage handle(Update update) {
        String url = ParserUtil.parseUrl(update.message().text());
        log.info("Вот такой url " + url);
        try {
            if (url.startsWith("https://github.com/")) {
                //TODO: обработка логики для https://github.com/
                LinkResponse link = scrapperClient.addLink(
                    update.message().chat().id(),
                    new AddLinkRequest(new URI(url)));
                String stringLog = String.format("Отслеживание id: %d, url: %s", link.id(), link.url());
                log.info(stringLog);
                return new SendMessage(update.message().chat().id(), stringLog);
            }
            return super.handle(update);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
