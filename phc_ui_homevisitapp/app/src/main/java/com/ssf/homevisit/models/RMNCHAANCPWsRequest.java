package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAANCPWsRequest {

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

    public RMNCHAANCPWsRequest(String uuid, String targetType,Integer stepCount) {
        this.relationshipType = "RESIDESIN";
        this.sourceType = "Citizen";
        this.sourceProperties = new SourceProperties();
        this.targetProperties = new TargetProperties(uuid);
        this.targetType = targetType;
        this.stepCount = stepCount;
    }

    public class SourceProperties {
        @SerializedName("isPregnant")
        @Expose
        private boolean isPregnant;
        @SerializedName("gender")
        @Expose
        private String gender;

        public SourceProperties() {
            this.isPregnant = true;
            this.gender = "Female";
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
