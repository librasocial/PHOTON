package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAPNCHouseHoldsRequest {

    @SerializedName("relationshipType")
    @Expose
    private String relationshipType;
    @SerializedName("sourceType")
    @Expose
    private String sourceType;
    @SerializedName("sourceProperties")
    @Expose
    private SourceProperties sourceProperties;
    @SerializedName("targetProperties")
    @Expose
    private TargetProperties targetProperties;
    @SerializedName("targetType")
    @Expose
    private String targetType;
    @SerializedName("stepCount")
    @Expose
    private int stepCount;

    public RMNCHAPNCHouseHoldsRequest(String uuid) {
        this.relationshipType = "CONTAINEDINPLACE";
        this.sourceType = "HouseHold";
        this.sourceProperties = new SourceProperties(true);
        this.targetProperties = new TargetProperties(uuid);
        this.targetType = "Village";
        this.stepCount = 1;
    }

    public class SourceProperties {
        @SerializedName("hasNewBorn")
        @Expose
        private boolean hasNewBorn;

        public SourceProperties(boolean hasDelivered) {
            this.hasNewBorn = hasDelivered;
        }
    }

    public class TargetProperties {
        @SerializedName("uuid")
        @Expose
        private String uuid;

        public TargetProperties(String uuid) {
            this.uuid = uuid;
        }
    }

}
