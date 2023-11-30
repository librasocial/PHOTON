package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchHouseholdBody {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("page")
    @Expose
    private long page;
    @SerializedName("size")
    @Expose
    private long size;
    @SerializedName("properties")
    @Expose
    private SearchHouseholdBodyProperty properties;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchHouseholdBody() {
    }

    /**
     *
     * @param size
     * @param page
     * @param type
     * @param properties
     */
    public SearchHouseholdBody(String type, long page, long size, SearchHouseholdBodyProperty properties) {
        super();
        this.type = type;
        this.page = page;
        this.size = size;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public SearchHouseholdBodyProperty getProperties() {
        return properties;
    }

    public void setProperties(SearchHouseholdBodyProperty properties) {
        this.properties = properties;
    }

}
