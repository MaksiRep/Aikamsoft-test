package ru.maksirep.aikam_test.dto;

public final class ErrorResponse {

    private final String type;
    private final String message;

    public ErrorResponse(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
