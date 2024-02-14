package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.UserMessageService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component public class LinkTrackerBot extends TelegramBot implements Bot {

    private final UserMessageService userMessageService;

    public LinkTrackerBot(
        ApplicationConfig applicationConfig, UserMessageService userMessageService, List<Command> commandList
    ) {
        super(applicationConfig.telegramToken());
        this.userMessageService = userMessageService;
        start();
        setMyCommands(commandList);
    }

    private void serve(Update update) {
        this.execute(userMessageService.process(update));
    }

    @Override public int process(List<Update> updates) {
        updates.forEach(this::serve);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override public void start() {
        this.setUpdatesListener(this::process);
    }

    @Override public void close() {
        this.close();
    }

    public void setMyCommands(List<Command> commandList) {
        List<BotCommand> botApiCommands = commandList.stream().map(Command::toApiCommand).toList();
        this.execute(new SetMyCommands(botApiCommands.toArray(BotCommand[]::new)));
    }
}
