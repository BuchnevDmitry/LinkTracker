package edu.java.bot.service;

import edu.java.bot.command.Command;
import java.util.List;

public interface CommandService {
    List<Command> commands();

    Command getCommand(String commandName);
}
