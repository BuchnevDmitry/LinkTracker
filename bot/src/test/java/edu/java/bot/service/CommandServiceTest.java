package edu.java.bot.service;

import edu.java.bot.api.exception.NotFoundException;
import edu.java.bot.command.Command;
import edu.java.bot.command.impl.HelpCommand;
import edu.java.bot.command.impl.ListCommand;
import edu.java.bot.command.impl.StartCommand;
import edu.java.bot.command.impl.TrackCommand;
import edu.java.bot.command.impl.UntackCommand;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommandServiceTest {
    @InjectMocks
    private CommandService commandService;
    @Spy
    private List<Command> commandList = List.of(new ListCommand(null), new StartCommand(null), new UntackCommand(
        null), new TrackCommand(null), new HelpCommand());

    @Test
    void getCommand_shouldGetTrackCommandClass_whenCommandNameIsTrack () {
        Command command = commandService.getCommand("/track");
        Assertions.assertEquals(TrackCommand.class, command.getClass());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "/command")
    void getCommand_shouldThrowException_whenCommandIsNotExist(String string) {
        Assertions.assertThrows(NotFoundException.class, () -> commandService.getCommand(string));
    }

    @ParameterizedTest
    @MethodSource("provideStringAndClassForGetCommand")
    void getCommand_shouldReturnCommandClass(String commandName, Class<? extends Command> commandClass) {
        Command resultCommand = commandService.getCommand(commandName);
        Assertions.assertEquals(resultCommand.getClass(), commandClass);
    }

    private static Stream<Arguments> provideStringAndClassForGetCommand() {
        return Stream.of(
            Arguments.of("/start", StartCommand.class),
            Arguments.of("/list", ListCommand.class),
            Arguments.of("/track", TrackCommand.class),
            Arguments.of("/untrack", UntackCommand.class),
            Arguments.of("/help", HelpCommand.class)
        );
    }
}
