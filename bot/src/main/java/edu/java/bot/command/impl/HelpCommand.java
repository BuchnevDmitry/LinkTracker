package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import java.util.List;
import org.springframework.stereotype.Component;

@Component public class HelpCommand implements Command {

    private List<Command> commandList;

    public HelpCommand(List<Command> commandList) {
        this.commandList = commandList;
    }

    public HelpCommand() {
    }

    @Override public String command() {
        return "/help";
    }

    @Override public String description() {
        return "Узнать все команды";
    }

    @Override public SendMessage handle(Update update) {
        StringBuilder commandText = new StringBuilder();
        String delimiter = " -- ";
        commandList.forEach(command -> commandText.append(command.command()).append(delimiter)
            .append(command.description()).append("\n"));
        commandText.append(this.command()).append(delimiter).append(this.description()).append("\n");
        return new SendMessage(update.message().chat().id(), commandText.toString());
    }
}
