package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VillageProperties implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("lgdCode")
    @Expose
    private String lgdCode;
    @SerializedName("household_count")
    @Expose
    private String householdCount;
    @SerializedName("in_area_count")
    @Expose
    private String inAreaCount;
    @SerializedName("out_of_area_count")
    @Expose
    private String outOfAreaCount;
    @SerializedName("total_population")
    @Expose
    private String totalPopulation;

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

    public String getLgdCode() {
        return lgdCode;
    }

    public void setLgdCode(String lgdCode) {
        this.lgdCode = lgdCode;
    }
}

