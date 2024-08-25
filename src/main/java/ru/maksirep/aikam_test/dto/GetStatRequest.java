package ru.maksirep.aikam_test.dto;

import java.time.LocalDate;

public class GetStatRequest {

    private LocalDate startDate;
    private LocalDate endDate;

    public GetStatRequest() {
    }

    public GetStatRequest(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
