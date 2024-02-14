package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.BotUtil.REGISTRATION;
@Slf4j
@Component
public class StartCommand implements Command {

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Регистрация";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("Запрос в базу данных на проверку регистрации пользователя");
        SendMessage response = new SendMessage(update.message().chat().id(), REGISTRATION);
        return response;
    }
}
