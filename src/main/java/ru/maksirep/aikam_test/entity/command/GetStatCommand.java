package ru.maksirep.aikam_test.entity.command;

import ru.maksirep.aikam_test.error.ServiceException;
import ru.maksirep.aikam_test.repository.Repository;

public class GetStatCommand implements Command {

    private final Repository repository;

    public GetStatCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() throws ServiceException {
        repository.getStat();
    }
}