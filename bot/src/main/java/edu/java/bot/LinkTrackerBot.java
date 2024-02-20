package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.UserMessageService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.stereotype.Component;


@Component
public class LinkTrackerBot extends TelegramBot {

    private final UserMessageService userMessageService;
    private final List<Command> commandList;

    public LinkTrackerBot(
        ApplicationConfig applicationConfig, UserMessageService userMessageService, List<Command> commandList
    ) {
        super(applicationConfig.telegramToken());
        this.userMessageService = userMessageService;
        this.commandList = commandList;
    }

    @PostConstruct
    void init() {
        start();
        setMyCommands(commandList);
    }

    private void serve(Update update) {
        if (update.message() != null) {
            this.execute(userMessageService.process(update));
        }
    }

    public int process(List<Update> updates) {
        updates.forEach(this::serve);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void start() {
        this.setUpdatesListener(this::process);
    }

    public void setMyCommands(List<Command> commandList) {
        List<BotCommand> botApiCommands = commandList.stream().map(Command::toApiCommand).toList();
        this.execute(new SetMyCommands(botApiCommands.toArray(BotCommand[]::new)));
    }
}
