package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCitizenBody {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private CitizenProperties properties;

    /**
     * No args constructor for use in serialization
     *
     */
    public CreateCitizenBody() {
    }

    /**
     *
     * @param type
     * @param properties
     */
    public CreateCitizenBody(String type, CitizenProperties properties) {
        super();
        this.type = type;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CitizenProperties getProperties() {
        return properties;
    }

    public void setProperties(CitizenProperties properties) {
        this.properties = properties;
    }

}