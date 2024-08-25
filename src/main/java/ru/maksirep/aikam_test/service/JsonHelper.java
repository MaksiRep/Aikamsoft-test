package ru.maksirep.aikam_test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.maksirep.aikam_test.error.ErrorType;
import ru.maksirep.aikam_test.error.ServiceException;

import java.io.File;
import java.io.IOException;

public class JsonHelper {

    private final FileHelper fileHelper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public <T> T getJsonFromFile(Class<T> clazz) {
        objectMapper.registerModule(new JavaTimeModule());
        File inputFile = fileHelper.getReadFile();
        T json;
        try {
            json = objectMapper.readValue(
                    inputFile,
                    clazz
            );
        } catch (IOException e) {
            throw new ServiceException(
                    ErrorType.ERROR, "Некорректный формат входного файла, проверьте правильность ввода полей");
        }
        return json;
    }

    public <T> void writeJsonToFile(T clazz) {
        try {
            objectMapper.writeValue(fileHelper.getWriteFile(), clazz);
        } catch (IOException e) {
            throw new ServiceException(ErrorType.ERROR, "Некорректный формат JSON файла");
        }
    }
}
