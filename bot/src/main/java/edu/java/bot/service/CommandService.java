package edu.java.bot.service;

import edu.java.bot.api.exception.NotFoundException;
import edu.java.bot.command.Command;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import static edu.java.bot.util.BotMessages.COMMAND_NOT_FOUND;

@Service
public class CommandService {

    private final List<Command> commandList;

    private final HashMap<String, Command> commandMap;

    public CommandService(
        List<Command> commandList
    ) {
        this.commandList = commandList;
        commandMap = (HashMap<String, Command>) commandList.stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));
    }

    public List<Command> commands() {
        return commandList;
    }

    public Command getCommand(String commandName) {
        Command command = commandMap.get(commandName);
        if (command == null) {
            throw new NotFoundException(COMMAND_NOT_FOUND);
        }
        return command;
    }
}
