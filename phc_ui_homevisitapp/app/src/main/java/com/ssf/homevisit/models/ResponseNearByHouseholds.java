package com.ssf.homevisit.models;

import java.util.List;

public class ResponseNearByHouseholds {
    private Integer totalPages;
    private Integer totalElements;
    private List<HouseHoldProperties> content;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public List<HouseHoldProperties> getContent() {
        return content;
    }

    public void setContent(List<HouseHoldProperties> content) {
        this.content = content;
    }
}
