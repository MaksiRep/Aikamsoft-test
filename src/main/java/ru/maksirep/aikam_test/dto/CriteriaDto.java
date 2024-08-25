package ru.maksirep.aikam_test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CriteriaDto {

    private String lastName;
    private String productName;
    private Integer minTimes;
    private Integer minExpenses;
    private Integer maxExpenses;
    private Integer badCustomers;

    public CriteriaDto() {
    }

    public CriteriaDto(String lastName,
                       String productName,
                       Integer minTimes,
                       Integer minExpenses,
                       Integer maxExpenses,
                       Integer badCustomers) {
        this.lastName = lastName;
        this.productName = productName;
        this.minTimes = minTimes;
        this.minExpenses = minExpenses;
        this.maxExpenses = maxExpenses;
        this.badCustomers = badCustomers;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getMinTimes() {
        return minTimes;
    }

    public void setMinTimes(Integer minTimes) {
        this.minTimes = minTimes;
    }

    public Integer getMinExpenses() {
        return minExpenses;
    }

    public void setMinExpenses(Integer minExpenses) {
        this.minExpenses = minExpenses;
    }

    public Integer getMaxExpenses() {
        return maxExpenses;
    }

    public void setMaxExpenses(Integer maxExpenses) {
        this.maxExpenses = maxExpenses;
    }

    public Integer getBadCustomers() {
        return badCustomers;
    }

    public void setBadCustomers(Integer badCustomers) {
        this.badCustomers = badCustomers;
    }
}
