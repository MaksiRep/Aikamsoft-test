package ru.maksirep.aikam_test.entity.command;

import ru.maksirep.aikam_test.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandFactory(Repository repository) {
        commandMap.put("search", new SearchCommand(repository));
        commandMap.put("stat", new GetStatCommand(repository));
    }

    public Command getCommand(String commandName) {
        return commandMap.getOrDefault(commandName.toLowerCase(), null);
    }
}