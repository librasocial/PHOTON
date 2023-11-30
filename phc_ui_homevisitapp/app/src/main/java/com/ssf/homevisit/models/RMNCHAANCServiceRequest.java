package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAANCServiceRequest {

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
    @SerializedName("deliverDate")
    @Expose
    public String deliverDate;
    @SerializedName("ancFacilityName")
    @Expose
    public String ancFacilityName;
    @SerializedName("dataEntryStatus")
    @Expose
    public String dataEntryStatus;
    @SerializedName("couple")
    @Expose
    public Couple couple;

    public RMNCHAANCServiceRequest(String rchId, String ashaWorker,
                                   String lmpDate, String eddDate,
                                   String ancFacilityType, String ancFacilityName,
                                   String wifeId, String husbandId,String deliveryDate) {
        this.couple = new Couple(wifeId, husbandId);
        this.rchId = rchId;
        this.ashaWorker = ashaWorker;
        this.lmpDate = lmpDate;
        this.eddDate = eddDate;
        this.ancFacilityType = ancFacilityType;
        this.ancFacilityName = ancFacilityName;
        this.dataEntryStatus = "COMPLETED";
        this.deliverDate = deliveryDate;
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

        public Couple(String wifeId, String husbandId) {
            this.wifeId = wifeId;
            this.husbandId = husbandId;
        }
    }

}
