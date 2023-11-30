package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreatePlaceResponse {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("assetType")
    @Expose
    private String assetType;
    @SerializedName("imgUrl")
    @Expose
    private List<String> imgUrl;
    @SerializedName("latitude")
    @Expose
    private Float latitude;
    @SerializedName("longitude")
    @Expose
    private Float longitude;
    @SerializedName("requiredSurveys")
    @Expose
    private String requiredSurveys;
    @SerializedName("village")
    @Expose
    private VillageProperties villageProperties;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getRequiredSurveys() {
        return requiredSurveys;
    }

    public void setRequiredSurveys(String requiredSurveys) {
        this.requiredSurveys = requiredSurveys;
    }

    public VillageProperties getVillageProperties() {
        return villageProperties;
    }

    public void setVillageProperties(VillageProperties villageProperties) {
        this.villageProperties = villageProperties;
    }
}
