package edu.java.bot.handler.link;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperClient;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HandlerLink {
    private HandlerLink next;

    protected ScrapperClient scrapperClient;

    protected void setScrapperClient(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    public SendMessage handle(Update update) throws URISyntaxException {
        if (next != null) {
            log.info("Передача ссылки в обработчик " + next.getClass().getName());
            return next.handle(update);
        }
        String stringLog = "Нет обработчика на данный url!";
        log.info(stringLog);
        return new SendMessage(update.message().chat().id(), stringLog);
    }


    public HandlerLink bind(HandlerLink next) {
        this.next = next;
        return next;
    }
}
