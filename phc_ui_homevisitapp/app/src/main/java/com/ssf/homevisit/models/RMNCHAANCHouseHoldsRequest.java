package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAANCHouseHoldsRequest {

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

    public RMNCHAANCHouseHoldsRequest(String uuid) {
        this.relationshipType = "CONTAINEDINPLACE";
        this.sourceType = "HouseHold";
        this.sourceProperties = new SourceProperties(true);
        this.targetProperties = new TargetProperties(uuid);
        this.targetType = "Village";
        this.stepCount = 1;
    }

    public class SourceProperties {
        @SerializedName("hasPregnantWoman")
        @Expose
        private boolean hasPregnantWoman;

        public SourceProperties(boolean hasPregnantWoman) {
            this.hasPregnantWoman = hasPregnantWoman;
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
