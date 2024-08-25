package ru.maksirep.aikam_test.error;

public enum ErrorType {
    ERROR("error");

    private final String value;

    ErrorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
