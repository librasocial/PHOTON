package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PHCProperties implements Serializable {
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("lattitude")
    @Expose
    private Double lattitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dateModified")
    @Expose
    private String dateModified;
    @SerializedName("modifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("longitude")
    @Expose
    private Double longitude;


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

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
