package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.exception.NotFoundException;
import edu.java.bot.api.exception.ParseException;
import edu.java.bot.api.exception.ResponseException;
import edu.java.bot.util.ParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static edu.java.bot.util.BotMessages.COMMAND_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMessageService {

    private final CommandService commandService;

    public SendMessage process(Update update) {
        try {
            String commandName = ParserUtil.parseCommandName(update.message().text());
            return commandService.getCommand(commandName).handle(update);
        } catch (ParseException | NotFoundException e) {
            log.info(String.format("%s %s", COMMAND_NOT_FOUND, e.getMessage()));
            return new SendMessage(update.message().chat().id(), COMMAND_NOT_FOUND).parseMode(ParseMode.HTML);
        } catch (ResponseException e) {
            String message = "Ошибка выполнения запроса -> " + e.getMessage();
            log.info(message);
            return new SendMessage(update.message().chat().id(), message);
        }
    }
}
