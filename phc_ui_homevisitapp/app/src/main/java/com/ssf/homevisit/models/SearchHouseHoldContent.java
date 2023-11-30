package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchHouseHoldContent {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("labels")
    @Expose
    private List<String> labels = null;
    @SerializedName("properties")
    @Expose
    private SearchHouseholdProperties properties;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchHouseHoldContent() {
    }

    /**
     *
     * @param id
     * @param properties
     * @param labels
     */
    public SearchHouseHoldContent(long id, List<String> labels, SearchHouseholdProperties properties) {
        super();
        this.id = id;
        this.labels = labels;
        this.properties = properties;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public SearchHouseholdProperties getProperties() {
        return properties;
    }

    public void setProperties(SearchHouseholdProperties properties) {
        this.properties = properties;
    }

}
