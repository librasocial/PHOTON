package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceProperties {

    @SerializedName("imgUrl")
    @Expose
    private List<String> imgUrl;
    @SerializedName("requiredSurveys")
    @Expose
    private String requiredSurveys;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("assetType")
    @Expose
    private String assetType;
    @SerializedName("assetSubType")
    @Expose
    private String assetSubType;
    @SerializedName("village")
    @Expose
    private String villageId;

    public String getAssetSubType() {
        return assetSubType;
    }

    public void setAssetSubType(String assetSubType) {
        this.assetSubType = assetSubType;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRequiredSurveys() {
        return requiredSurveys;
    }

    public void setRequiredSurveys(String requiredSurveys) {
        this.requiredSurveys = requiredSurveys;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
