package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.handler.link.BindHandlerLink;
import edu.java.bot.model.request.AddLinkRequest;
import edu.java.bot.model.response.LinkResponse;
import edu.java.bot.util.ParserUtil;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static edu.java.bot.util.BotUtil.HANDLER_NOT_FOUND;

@Slf4j
@Service
public class LinkService {

    @Autowired
    private ScrapperClient scrapperClient;
    private final BindHandlerLink bindHandlerLink;

    public LinkService(BindHandlerLink bindHandlerLink) {
        this.bindHandlerLink = bindHandlerLink;
    }

    public SendMessage addLink(Update update) throws URISyntaxException {
        String url = ParserUtil.parseUrl(update.message().text());
        log.info("Вот такой url " + url);
        if (bindHandlerLink.binding().handle(url)) {
            LinkResponse link = scrapperClient.addLink(
                update.message().chat().id(),
                new AddLinkRequest(new URI(url)));
            String stringLog = String.format("Отслеживание id: %d, url: %s", link.id(), link.url());
            log.info(stringLog);
            return new SendMessage(update.message().chat().id(), stringLog);
        }
        log.info(HANDLER_NOT_FOUND);
        return new SendMessage(update.message().chat().id(), HANDLER_NOT_FOUND);
    }
}
