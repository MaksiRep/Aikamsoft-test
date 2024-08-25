package ru.maksirep.aikam_test.dto;

import java.util.List;

public class GetStatResponse {

    private String type;
    private long totalDays;
    private List<GetStatResponseCustomers> customers;
    private int totalExpenses;
    private double avgExpenses;

    public class GetStatResponseCustomers {

        private String name;
        private List<GetStatResponsePurchase> purchases;
        private int totalExpenses;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<GetStatResponsePurchase> getPurchases() {
            return purchases;
        }

        public void setPurchases(List<GetStatResponsePurchase> purchases) {
            this.purchases = purchases;
        }

        public int getTotalExpenses() {
            return totalExpenses;
        }

        public void setTotalExpenses(int totalExpenses) {
            this.totalExpenses = totalExpenses;
        }

        public class GetStatResponsePurchase {

            private String name;
            private int expenses;

            public GetStatResponsePurchase(String name, int expenses) {
                this.name = name;
                this.expenses = expenses;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getExpenses() {
                return expenses;
            }

            public void setExpenses(int expenses) {
                this.expenses = expenses;
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(long totalDays) {
        this.totalDays = totalDays;
    }

    public List<GetStatResponseCustomers> getCustomers() {
        return customers;
    }

    public void setCustomers(List<GetStatResponseCustomers> customers) {
        this.customers = customers;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getAvgExpenses() {
        return avgExpenses;
    }

    public void setAvgExpenses(double avgExpenses) {
        this.avgExpenses = avgExpenses;
    }
}
