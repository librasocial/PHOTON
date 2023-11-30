package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAPNCMotherServiceResponse {

    @SerializedName("id")
    @Expose
    public String id;
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

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Couple getCouple() {
        return couple;
    }

    public void setCouple(Couple couple) {
        this.couple = couple;
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
    }

}
