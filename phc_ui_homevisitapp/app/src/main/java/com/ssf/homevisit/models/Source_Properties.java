package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source_Properties {

    @SerializedName("in_area_count")
    @Expose
    private long inAreaCount;
    @SerializedName("household_count")
    @Expose
    private long householdCount;
    @SerializedName("lattitude")
    @Expose
    private String lattitude;
    @SerializedName("dateModified")
    @Expose
    private String dateModified;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("lgd_code")
    @Expose
    private String lgdCode;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("modifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("total_population")
    @Expose
    private long totalPopulation;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    /**
     * No args constructor for use in serialization
     *
     */
    public Source_Properties() {
    }

    /**
     *
     * @param lgdCode
     * @param dateCreated
     * @param totalPopulation
     * @param lattitude
     * @param createdBy
     * @param name
     * @param householdCount
     * @param dateModified
     * @param modifiedBy
     * @param inAreaCount
     * @param uuid
     * @param longitude
     */
    public Source_Properties(long inAreaCount, long householdCount, String lattitude, String dateModified, String uuid, String lgdCode, String dateCreated, String createdBy, String name, String modifiedBy, long totalPopulation, String longitude) {
        super();
        this.inAreaCount = inAreaCount;
        this.householdCount = householdCount;
        this.lattitude = lattitude;
        this.dateModified = dateModified;
        this.uuid = uuid;
        this.lgdCode = lgdCode;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.name = name;
        this.modifiedBy = modifiedBy;
        this.totalPopulation = totalPopulation;
        this.longitude = longitude;
    }

    public long getInAreaCount() {
        return inAreaCount;
    }

    public void setInAreaCount(long inAreaCount) {
        this.inAreaCount = inAreaCount;
    }

    public long getHouseholdCount() {
        return householdCount;
    }

    public void setHouseholdCount(long householdCount) {
        this.householdCount = householdCount;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLgdCode() {
        return lgdCode;
    }

    public void setLgdCode(String lgdCode) {
        this.lgdCode = lgdCode;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public long getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(long totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}