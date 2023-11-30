package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchHouseholdResponse {

    @SerializedName("totalPages")
    @Expose
    private int totalPages;
    @SerializedName("totalElements")
    @Expose
    private int totalElements;
    @SerializedName("content")
    @Expose
    private List<SearchHouseHoldContent> content = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchHouseholdResponse() {
    }

    /**
     *
     * @param totalPages
     * @param content
     * @param totalElements
     */
    public SearchHouseholdResponse(int totalPages, int totalElements, List<SearchHouseHoldContent> content) {
        super();
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public List<SearchHouseHoldContent> getContent() {
        return content;
    }

    public void setContent(List<SearchHouseHoldContent> content) {
        this.content = content;
    }
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



//start
        public class SearchHouseholdProperties {

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("houseHeadName")
    @Expose
    private String houseHeadName;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("houseID")
    @Expose
    private String houseID;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("modifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("dateModified")
    @Expose
    private String dateModified;

    @SerializedName("totalFamilyMembers")
    @Expose
    private Integer totalFamilyMembers;

    public Integer getTotalFamilyMembers() {
        return totalFamilyMembers;
    }

    public void setTotalFamilyMembers(Integer totalFamilyMembers) {
        this.totalFamilyMembers = totalFamilyMembers;
    }

    public String getHouseHeadName() {
        return houseHeadName;
    }

    public void setHouseHeadName(String houseHeadName) {
        this.houseHeadName = houseHeadName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
        }

//end
    }

}
