package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateResidentBody {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private ResidentProperties properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ResidentProperties getProperties() {
        return properties;
    }

    public void setProperties(ResidentProperties properties) {
        this.properties = properties;
    }
}
