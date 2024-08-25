package ru.maksirep.aikam_test.error;

public class ServiceException extends RuntimeException {

    private final ErrorType type;
    private final String message;

    public ServiceException(ErrorType type, String message) {
        super(message);
        this.message = message;
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
