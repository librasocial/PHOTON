package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateVillageAssetBody {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private PlaceProperties placeProperties;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PlaceProperties getPlaceProperties() {
        return placeProperties;
    }

    public void setPlaceProperties(PlaceProperties placeProperties) {
        this.placeProperties = placeProperties;
    }
}
