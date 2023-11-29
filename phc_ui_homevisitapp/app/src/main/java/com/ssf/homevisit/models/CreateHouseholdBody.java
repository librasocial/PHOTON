package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateHouseholdBody {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private HouseHoldProperties properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HouseHoldProperties getProperties() {
        return properties;
    }

    public void setProperties(HouseHoldProperties properties) {
        this.properties = properties;
    }

}
