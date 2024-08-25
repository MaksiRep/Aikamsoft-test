package ru.maksirep.aikam_test;

import org.flywaydb.core.Flyway;
import ru.maksirep.aikam_test.config.ConfigLoader;
import ru.maksirep.aikam_test.entity.command.Command;
import ru.maksirep.aikam_test.entity.command.CommandFactory;
import ru.maksirep.aikam_test.error.ErrorHandler;
import ru.maksirep.aikam_test.error.ErrorType;
import ru.maksirep.aikam_test.error.ServiceException;
import ru.maksirep.aikam_test.service.FileHelper;
import ru.maksirep.aikam_test.service.JsonHelper;
import ru.maksirep.aikam_test.repository.Repository;

import java.io.*;

public class Main {

    static {
        ConfigLoader configLoader = new ConfigLoader("application.properties");
        String dbUrl = configLoader.getProperty("db.url");
        String dbUsername = configLoader.getProperty("db.username");
        String dbPassword = configLoader.getProperty("db.password");
        String flywayLocations = configLoader.getProperty("flyway.locations");
        Flyway flyway = Flyway.configure()
                .dataSource(dbUrl, dbUsername, dbPassword)
                .locations(flywayLocations)
                .load();
        flyway.migrate();
    }

    public static void main(String[] args) {
        String consoleCommand;
        String inputFilePath;
        String outputFilePath;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Введите команду");
                consoleCommand = bufferedReader.readLine();
                if (consoleCommand != null && consoleCommand.equals("0")) {
                    return;
                }
                System.out.println("Введите путь входного файла");
                inputFilePath = bufferedReader.readLine();
                if (inputFilePath != null && inputFilePath.equals("0")) {
                    return;
                }
                System.out.println("Введите путь выходного файла");
                outputFilePath = bufferedReader.readLine();
                if (outputFilePath != null && outputFilePath.equals("0")) {
                    return;
                }
                FileHelper fileHelper = new FileHelper(outputFilePath, inputFilePath);
                ErrorHandler errorHandler = new ErrorHandler(fileHelper);
                JsonHelper jsonHelper = new JsonHelper(fileHelper);
                Repository repository = new Repository(jsonHelper);
                if (consoleCommand != null && !consoleCommand.isEmpty()) {
                    Command command = new CommandFactory(repository).getCommand(consoleCommand);
                    if (command != null) {
                        try {
                            command.execute();
                        } catch (ServiceException ex) {
                            errorHandler.dropError(ex.getType(), ex.getMessage());
                        }
                    } else {
                        errorHandler.dropError(ErrorType.ERROR, "Некорректная команда");
                    }
                } else {
                    errorHandler.dropError(ErrorType.ERROR, "Пустая команда");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}