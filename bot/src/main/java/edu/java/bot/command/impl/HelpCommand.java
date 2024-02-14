package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class HelpCommand implements Command {

    private List<Command> commandList;
    public HelpCommand(List<Command> commandList) {
        this.commandList = commandList;
    }
    public HelpCommand() {}
    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Узнать все команды";
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder commandText = new StringBuilder();
        commandList.forEach(
                command -> commandText.append(command.command())
                    .append(" -- ")
                    .append(command.description()).append("\n")
            );
        commandText.append(this.command())
            .append(" -- ")
            .append(this.description()).append("\n");
        return new SendMessage(update.message().chat().id(), commandText.toString());
    }
}
