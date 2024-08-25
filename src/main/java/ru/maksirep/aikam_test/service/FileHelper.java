package ru.maksirep.aikam_test.service;

import ru.maksirep.aikam_test.error.ErrorType;
import ru.maksirep.aikam_test.error.ServiceException;

import java.io.File;
import java.io.IOException;

public class FileHelper {

    private static final String DEFAULT_ERROR_FILE = "src/main/resources/jsons/output/output.json";
    private final String outputFilePath;
    private final String inputFilePath;

    public FileHelper(String outputFilePath, String inputFilePath) {
        this.outputFilePath = outputFilePath;
        this.inputFilePath = inputFilePath;
    }

    public File getWriteFile () {
        if (outputFilePath == null || outputFilePath.isEmpty()) {
            throw new ServiceException(ErrorType.ERROR, "Пустое значения для выходного файла");
        }
        return writeFile(outputFilePath);
    }

    public File getReadFile () {
        if (inputFilePath == null || inputFilePath.isEmpty()) {
            throw new ServiceException(ErrorType.ERROR, "Пустое значения для входного файла");
        }
            File file = new File(inputFilePath);
            if (!file.exists()) {
                throw new ServiceException(ErrorType.ERROR, String.format("Файл с путем \"%s\" не найден", inputFilePath));
            }
            return file;
    }

    public File getDeafultErrorFile () {
        return writeFile(DEFAULT_ERROR_FILE);
    }

    private File writeFile (String path) {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            file.createNewFile();
            System.out.println(String.format("Результат исполнения программы %s",file.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new File(path);
    }
}
