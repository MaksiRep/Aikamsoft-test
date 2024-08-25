package ru.maksirep.aikam_test.dto;

import java.util.List;

public class SearchRequest {

    private List<CriteriaDto> criterias;

    public SearchRequest() {
    }

    public SearchRequest(List<CriteriaDto> criterias) {
        this.criterias = criterias;
    }

    public List<CriteriaDto> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<CriteriaDto> criterias) {
        this.criterias = criterias;
    }
}
