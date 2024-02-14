package edu.java.bot.service.impl;

import edu.java.bot.command.Command;
import edu.java.bot.service.CommandService;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommandServiceImpl implements CommandService {

    private final List<Command> commandList;

    private final HashMap<String, Command> commandMap;
    public CommandServiceImpl(
        List<Command> commandList
    ) {
        this.commandList = commandList;
        commandMap = (HashMap<String, Command>) commandList.stream().collect(Collectors.toMap(Command::command, Function.identity()));
    }

    @Override
    public List<Command> commands() {
        return commandList;
    }

    @Override
    public Command getCommand(String commandName) {
        return commandMap.get(commandName);
    }
}
