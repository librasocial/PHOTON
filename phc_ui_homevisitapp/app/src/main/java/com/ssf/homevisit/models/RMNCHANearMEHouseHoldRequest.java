package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHANearMEHouseHoldRequest {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("size")
    @Expose
    private int size;
    @SerializedName("properties")
    @Expose
    private RequestProperties properties;

    public RMNCHANearMEHouseHoldRequest(int page, int size, String villageID, Double latitude, Double longitude, Double distance) {
        this.type = "HouseHold";
        this.page = page;
        this.size = size;
        this.properties = new RequestProperties(villageID, latitude, longitude, distance);
    }

    public RMNCHANearMEHouseHoldRequest(String villageID, Double latitude, Double longitude) {
        this.type = "HouseHold";
        this.page = 0;
        this.size = 5000;
        this.properties = new RequestProperties(villageID, latitude, longitude, 5000.0);
    }

    public class RequestProperties {

        @SerializedName("villageId")
        @Expose
        private String villageId;
        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("distance")
        @Expose
        private Double distance;

        public RequestProperties(String villageId, Double latitude, Double longitude, Double distance) {
            this.villageId = villageId;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
        }

    }
}
