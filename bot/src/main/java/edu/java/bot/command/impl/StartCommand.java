package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.service.ScrapperClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.BotUtil.REGISTRATION;

@Slf4j
@Component
public class StartCommand implements Command {

    @Autowired
    private ScrapperClient scrapperClient;

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
        scrapperClient.registerChat(update.message().chat().id());
        SendMessage response = new SendMessage(update.message().chat().id(), REGISTRATION);
        return response;
    }
}
