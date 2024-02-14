package edu.java.bot.service.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import edu.java.bot.service.UserMessageService;
import edu.java.bot.util.ParserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static edu.java.bot.util.BotUtil.COMMAND_NOT_FOUNT;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMessageServiceImpl implements UserMessageService {

    private final CommandService commandService;

    @Override
    public SendMessage process(Update update) {
        try {
            String commandName = ParserUtils.parseCommandName(update.message().text());
            return commandService.getCommand(commandName).handle(update);
        }
        catch (NullPointerException e) {
            log.error(e.getMessage());
            log.info(COMMAND_NOT_FOUNT);
            return new SendMessage(update.message().chat().id(), COMMAND_NOT_FOUNT).parseMode(ParseMode.HTML);
        }
    }
}
