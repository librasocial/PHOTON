package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAPNCMotherServiceRequest {

    @SerializedName("rchId")
    @Expose
    public String rchId;
    @SerializedName("ashaWorker")
    @Expose
    public String ashaWorker;
    @SerializedName("deliveryType")
    @Expose
    public String deliveryType;
    @SerializedName("deliveryDate")
    @Expose
    public String deliveryDate;
    @SerializedName("couple")
    @Expose
    public Couple couple;

    public RMNCHAPNCMotherServiceRequest(String rchId, String ashaWorker,
                                         String deliveryType, String deliveryDate,
                                         String wifeId, String husbandId) {
        this.couple = new Couple(wifeId, husbandId);
        this.rchId = rchId;
        this.ashaWorker = ashaWorker;
        this.deliveryType = deliveryType;
        this.deliveryDate = deliveryDate;
    }

    public static class Couple {
        @SerializedName("wifeId")
        @Expose
        public String wifeId;
        @SerializedName("husbandId")
        @Expose
        public String husbandId;

        public Couple(String wifeId, String husbandId) {
            this.wifeId = wifeId;
            this.husbandId = husbandId;
        }
    }

}
