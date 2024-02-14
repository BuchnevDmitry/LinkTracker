package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.util.ParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.BotUtil.LINK_MISSING;

@Slf4j
@Component
public class UntackCommand implements Command {
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("Запрос в базу об информации по данной ссылке");
        try {
            String url = ParserUtils.parseUrl(update.message().text());
            log.info("Отслеживание " + url + " прекращено!");
            return new SendMessage(update.message().chat().id(), String.format("Отслеживание %s прекращено!", url));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return new SendMessage(update.message().chat().id(), LINK_MISSING);
        }
    }
}
