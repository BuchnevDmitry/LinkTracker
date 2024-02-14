package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.BotUtil.TRACK_LINKS_NOT_FOUNT;

@Slf4j
@Component
public class ListCommand implements Command {

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
        return new SendMessage(update.message().chat().id(), TRACK_LINKS_NOT_FOUNT).parseMode(ParseMode.HTML);
    }
}
