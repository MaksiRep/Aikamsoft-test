package ru.maksirep.aikam_test.repository;

import ru.maksirep.aikam_test.config.DatabaseConnection;
import ru.maksirep.aikam_test.dto.*;
import ru.maksirep.aikam_test.error.ErrorType;
import ru.maksirep.aikam_test.error.ServiceException;
import ru.maksirep.aikam_test.service.JsonHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private final JsonHelper jsonHelper;

    public Repository(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void getStat() {
        GetStatRequest getStatRequest = jsonHelper.getJsonFromFile(GetStatRequest.class);
        checkCorrectDate(getStatRequest);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String getUniquePurchases = getUniquePurchasesBetweenDate(
                    getStatRequest.getStartDate(),
                    getStatRequest.getEndDate());
            ArrayList<Integer> customersIds = new ArrayList<>();
            try (PreparedStatement preparedStatement = connection.prepareStatement(getUniquePurchases)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        customersIds.add(resultSet.getInt("customer_id"));
                    }
                }
            } catch (SQLException e) {
                throw new ServiceException(ErrorType.ERROR, "Некорректный SQL запрос");
            }
            int fullPrice = 0;
            int customerCount = 0;
            GetStatResponse getStatResponse = new GetStatResponse();
            getStatResponse.setType("stat");
            getStatResponse.setTotalDays(
                    ChronoUnit.DAYS.between(getStatRequest.getStartDate(), getStatRequest.getEndDate()) + 1);
            List<GetStatResponse.GetStatResponseCustomers> customersList = new ArrayList<>();
            for (Integer customerId : customersIds) {
                int customerPrice = 0;
                customerCount++;
                GetStatResponse.GetStatResponseCustomers getStatResponseCustomers =
                        getStatResponse.new GetStatResponseCustomers();
                String getCustomerPurchase = getCustomerPurchase(customerId);
                try (PreparedStatement preparedStatement = connection.prepareStatement(getCustomerPurchase)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        List<GetStatResponse.GetStatResponseCustomers.GetStatResponsePurchase> purchases =
                                new ArrayList<>();
                        while (resultSet.next()) {
                            if (getStatResponseCustomers.getName() == null ||
                                    getStatResponseCustomers.getName().isEmpty()) {
                                getStatResponseCustomers.setName(
                                        resultSet.getString("lastname") + " " + resultSet.getString("firstname"));
                            }
                            fullPrice += resultSet.getInt("price");
                            customerPrice += resultSet.getInt("price");
                            purchases.add(getStatResponseCustomers.new GetStatResponsePurchase(
                                    resultSet.getString("name"),
                                    resultSet.getInt("price")
                            ));
                        }
                        getStatResponseCustomers.setPurchases(purchases);
                        getStatResponseCustomers.setTotalExpenses(customerPrice);
                        customersList.add(getStatResponseCustomers);
                    }
                } catch (SQLException e) {
                    throw new ServiceException(ErrorType.ERROR, "Некорректный SQL запрос");
                }
            }
            getStatResponse.setCustomers(customersList);
            getStatResponse.setTotalExpenses(fullPrice);
            getStatResponse.setAvgExpenses((double) fullPrice / (customerCount == 0 ? 1 : customerCount));
            jsonHelper.writeJsonToFile(getStatResponse);
        } catch (SQLException e) {
            throw new ServiceException(ErrorType.ERROR, "Ошибка соединения с базой данных");
        }
    }

    public void search() {
        SearchRequest searchRequest = jsonHelper.getJsonFromFile(SearchRequest.class);
        if (searchRequest == null || searchRequest.getCriterias() == null) {
            throw new ServiceException(ErrorType.ERROR, "Некорректный формат Json");
        }
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setType("search");
        List<SearchResponse.SearchResponseResults> searchResponseResultsList = new ArrayList<>();
        for (CriteriaDto criteria : searchRequest.getCriterias()) {
            SearchResponse.SearchResponseResults searchResponseResults = searchResponse.new SearchResponseResults();
            searchResponseResults.setCriteria(criteria);
            checkCorrectCondition(criteria);
            try (Connection connection = DatabaseConnection.getConnection()) {
                String getSearchResult = getSearchResult(criteria);
                try (PreparedStatement preparedStatement = connection.prepareStatement(getSearchResult)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        List<CustomerDto> customers = new ArrayList<>();
                        while (resultSet.next()) {
                            customers.add(new CustomerDto(
                                    resultSet.getString("lastname"),
                                    resultSet.getString("firstname")
                            ));
                        }
                        searchResponseResults.setResults(customers);
                        searchResponseResultsList.add(searchResponseResults);
                    }
                } catch (SQLException e) {
                    throw new ServiceException(ErrorType.ERROR, "Некорректный SQL запрос");
                }
            } catch (SQLException e) {
                throw new ServiceException(ErrorType.ERROR, "Ошибка соединения с базой данных");
            }
        }
        searchResponse.setResults(searchResponseResultsList);
        jsonHelper.writeJsonToFile(searchResponse);
    }

    private String getSearchResult(CriteriaDto criteriaDto) {
        return String.format("SELECT c.lastname as lastname, c.firstname as firstname " +
                        "FROM purchase " +
                        "         JOIN customer c ON c.id = purchase.customer_id " +
                        "         JOIN product p ON p.id = purchase.product_id " +
                        "WHERE (%s IS NULL OR c.lastname = %s) " +
                        "  AND (%s IS NULL OR p.name = %s " +
                        "    AND c.id IN (SELECT t.id " +
                        "                 FROM (SELECT customer_id AS id, COUNT(*) AS count " +
                        "                       FROM purchase " +
                        "                       GROUP BY customer_id) t " +
                        "                 WHERE t.count >= %d)) " +
                        "  AND (%d IS NULL OR price BETWEEN %d AND %d) " +
                        "  AND (%d IS NULL OR c.id IN (SELECT t.id " +
                        "                                         FROM (SELECT customer_id AS id, COUNT(*) AS count " +
                        "                                               FROM purchase " +
                        "                                               GROUP BY customer_id " +
                        "                                               ORDER BY count) t " +
                        "                                         LIMIT %d)) " +
                        "GROUP BY c.lastname, c.firstname",
                criteriaDto.getLastName() == null ? null : String.format("'%s'",criteriaDto.getLastName()),
                criteriaDto.getLastName() == null ? null : String.format("'%s'",criteriaDto.getLastName()),
                criteriaDto.getProductName() == null ? null : String.format("'%s'",criteriaDto.getProductName()),
                criteriaDto.getProductName() == null ? null : String.format("'%s'",criteriaDto.getProductName()),
                criteriaDto.getMinTimes(),
                criteriaDto.getMinExpenses(), criteriaDto.getMinExpenses(), criteriaDto.getMaxExpenses(),
                criteriaDto.getBadCustomers(), criteriaDto.getBadCustomers());
    }

    private String getUniquePurchasesBetweenDate(LocalDate startDate, LocalDate endDate) {
        return String.format("SELECT customer_id " +
                        "FROM purchase " +
                        "WHERE purchase_date BETWEEN '%s' AND '%s' " +
                        "GROUP BY (customer_id) ",
                startDate.toString(),
                endDate.toString());
    }

    private String getCustomerPurchase(int customerId) {
        return String.format("SELECT firstname, lastname, name, price " +
                        "FROM purchase " +
                        "         JOIN customer c ON c.id = purchase.customer_id " +
                        "         JOIN product p ON p.id = purchase.product_id " +
                        "WHERE customer_id = %d " +
                        "ORDER BY price DESC ",
                customerId);
    }

    private void checkCorrectDate(GetStatRequest getStatRequest) {
        if (getStatRequest == null) {
            throw new ServiceException(ErrorType.ERROR, "Некорректный формат Json");
        }
        if (getStatRequest.getStartDate() == null || getStatRequest.getEndDate() == null) {
            throw new ServiceException(ErrorType.ERROR, "Пустые значения для одного или нескольких полей даты");
        }
        if (getStatRequest.getStartDate().isAfter(getStatRequest.getEndDate())) {
            throw new ServiceException(ErrorType.ERROR, "Начальная дата не может быть позже конечной даты");
        }
    }

    private void checkCorrectCondition(CriteriaDto criteriaDto) {
        if (criteriaDto == null) {
            throw new ServiceException(ErrorType.ERROR, "Некорректный формат Json");
        }
        if ((criteriaDto.getMinExpenses() != null && criteriaDto.getMaxExpenses() == null) ||
                (criteriaDto.getMinExpenses() == null && criteriaDto.getMaxExpenses() != null) ||
                (criteriaDto.getMinExpenses() != null && criteriaDto.getMaxExpenses() != null &&
                        (criteriaDto.getMaxExpenses() < criteriaDto.getMinExpenses() ||
                                criteriaDto.getMinExpenses() < 0 || criteriaDto.getMaxExpenses() < 0))) {
            throw new ServiceException(ErrorType.ERROR, "Некорректные максимальная и минимальная стоимость");
        }
        if ((criteriaDto.getProductName() != null && criteriaDto.getMinTimes() == null) ||
                (criteriaDto.getProductName() == null && criteriaDto.getMinTimes() != null) ||
                (criteriaDto.getProductName() != null && criteriaDto.getMinTimes() != null &&
                        criteriaDto.getMinTimes() < 0)) {
            throw new ServiceException(ErrorType.ERROR, "Некорректные название товара и число раз");
        }
    }
}