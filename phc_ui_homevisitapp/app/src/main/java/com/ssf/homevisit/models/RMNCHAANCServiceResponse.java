package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAANCServiceResponse {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rchId")
    @Expose
    public String rchId;
    @SerializedName("ashaWorker")
    @Expose
    public String ashaWorker;
    @SerializedName("lmpDate")
    @Expose
    public String lmpDate;
    @SerializedName("eddDate")
    @Expose
    public String eddDate;
    @SerializedName("ancFacilityType")
    @Expose
    public String ancFacilityType;
    @SerializedName("ancFacilityName")
    @Expose
    public String ancFacilityName;
    @SerializedName("dataEntryStatus")
    @Expose
    public String dataEntryStatus;
    @SerializedName("couple")
    @Expose
    public RMNCHAANCServiceRequest.Couple couple;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRchId() {
        return rchId;
    }

    public void setRchId(String rchId) {
        this.rchId = rchId;
    }

    public String getAshaWorker() {
        return ashaWorker;
    }

    public void setAshaWorker(String ashaWorker) {
        this.ashaWorker = ashaWorker;
    }

    public String getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(String lmpDate) {
        this.lmpDate = lmpDate;
    }

    public String getEddDate() {
        return eddDate;
    }

    public void setEddDate(String eddDate) {
        this.eddDate = eddDate;
    }

    public String getAncFacilityType() {
        return ancFacilityType;
    }

    public void setAncFacilityType(String ancFacilityType) {
        this.ancFacilityType = ancFacilityType;
    }

    public String getAncFacilityName() {
        return ancFacilityName;
    }

    public void setAncFacilityName(String ancFacilityName) {
        this.ancFacilityName = ancFacilityName;
    }

    public String getDataEntryStatus() {
        return dataEntryStatus;
    }

    public void setDataEntryStatus(String dataEntryStatus) {
        this.dataEntryStatus = dataEntryStatus;
    }

    public RMNCHAANCServiceRequest.Couple getCouple() {
        return couple;
    }

    public void setCouple(RMNCHAANCServiceRequest.Couple couple) {
        this.couple = couple;
    }

    public RMNCHAANCServiceResponse() {
    }

    public static class Couple {
        @SerializedName("wifeId")
        @Expose
        public String wifeId;
        @SerializedName("husbandId")
        @Expose
        public String husbandId;

        public String getWifeId() {
            return wifeId;
        }

        public void setWifeId(String wifeId) {
            this.wifeId = wifeId;
        }

        public String getHusbandId() {
            return husbandId;
        }

        public void setHusbandId(String husbandId) {
            this.husbandId = husbandId;
        }

        public Couple() {
        }
    }
}
