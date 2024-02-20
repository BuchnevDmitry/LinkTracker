package edu.java.bot.handler.link;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HandlerLink {
    private HandlerLink next;

    public SendMessage handle(Update update) {
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
