package ru.maksirep.aikam_test.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.maksirep.aikam_test.dto.ErrorResponse;
import ru.maksirep.aikam_test.service.FileHelper;

import java.io.IOException;

public class ErrorHandler {

    private final FileHelper fileHelper;

    public ErrorHandler(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public void dropError(ErrorType type, String message) {
        ErrorResponse errorResponse = new ErrorResponse(type.getValue(), message);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(fileHelper.getWriteFile(), errorResponse);
        } catch (Exception e) {
            try {
                objectMapper.writeValue(fileHelper.getDeafultErrorFile(), errorResponse);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
