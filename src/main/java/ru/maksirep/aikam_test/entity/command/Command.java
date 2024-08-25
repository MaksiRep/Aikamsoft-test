package ru.maksirep.aikam_test.entity.command;

import ru.maksirep.aikam_test.error.ServiceException;

public interface Command {
    void execute () throws ServiceException;
}
