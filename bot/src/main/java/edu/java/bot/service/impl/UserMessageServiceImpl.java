package edu.java.bot.service.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.exception.ResponseException;
import edu.java.bot.service.CommandService;
import edu.java.bot.service.UserMessageService;
import edu.java.bot.util.ParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static edu.java.bot.util.BotUtil.COMMAND_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMessageServiceImpl implements UserMessageService {

    private final CommandService commandService;

    @Override
    public SendMessage process(Update update) {
        try {
            String commandName = ParserUtil.parseCommandName(update.message().text());
            return commandService.getCommand(commandName).handle(update);
        } catch (NullPointerException e) {
            log.info(String.format("%s %s", COMMAND_NOT_FOUND, e.getMessage()));
            return new SendMessage(update.message().chat().id(), COMMAND_NOT_FOUND).parseMode(ParseMode.HTML);
        } catch (ResponseException e) {
            log.info("Ошибка запроса -> " + e.getMessage());
            return new SendMessage(update.message().chat().id(), e.getMessage());
        }
    }
}
