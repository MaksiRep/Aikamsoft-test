package ru.maksirep.aikam_test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResponse {

    private String type;
    private List<SearchResponseResults> results;

    public SearchResponse() {
    }

    public SearchResponse(String type, List<SearchResponseResults> results) {
        this.type = type;
        this.results = results;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class SearchResponseResults {

        private CriteriaDto criteria;
        private List<CustomerDto> results;

        public SearchResponseResults() {
        }

        public SearchResponseResults(CriteriaDto criteria, List<CustomerDto> results) {
            this.criteria = criteria;
            this.results = results;
        }

        public CriteriaDto getCriteria() {
            return criteria;
        }

        public void setCriteria(CriteriaDto criteria) {
            this.criteria = criteria;
        }

        public List<CustomerDto> getResults() {
            return results;
        }

        public void setResults(List<CustomerDto> results) {
            this.results = results;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SearchResponseResults> getResults() {
        return results;
    }

    public void setResults(List<SearchResponseResults> results) {
        this.results = results;
    }
}
