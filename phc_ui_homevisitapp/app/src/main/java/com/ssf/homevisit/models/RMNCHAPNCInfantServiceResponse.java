package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAPNCInfantServiceResponse {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("childId")
    @Expose
    public String childId;
    @SerializedName("pncServiceId")
    @Expose
    public String pncServiceId;
    @SerializedName("ashaWorker")
    @Expose
    public String ashaWorker;
    @SerializedName("birthWeight")
    @Expose
    public String birthWeight;
    @SerializedName("dateOfBirth")
    @Expose
    public String dateOfBirth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getPncServiceId() {
        return pncServiceId;
    }

    public void setPncServiceId(String pncServiceId) {
        this.pncServiceId = pncServiceId;
    }

    public String getAshaWorker() {
        return ashaWorker;
    }

    public void setAshaWorker(String ashaWorker) {
        this.ashaWorker = ashaWorker;
    }

    public String getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(String birthWeight) {
        this.birthWeight = birthWeight;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
