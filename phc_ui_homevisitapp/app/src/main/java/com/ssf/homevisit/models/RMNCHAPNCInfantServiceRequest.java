package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAPNCInfantServiceRequest {

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

    public RMNCHAPNCInfantServiceRequest(String childId, String pncServiceId, String ashaWorker) {
        this.childId = childId;
        this.pncServiceId = pncServiceId;
        this.ashaWorker = ashaWorker;
        this.birthWeight = birthWeight;
        this.dateOfBirth = dateOfBirth;

    }

}
