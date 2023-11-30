package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAMotherInfantDetailsRequest {

    @SerializedName("relationshipType")
    @Expose
    private String relationshipType;
    @SerializedName("sourceType")
    @Expose
    private String sourceType;
    @SerializedName("sourceProperties")
    @Expose
    private SourceProperties sourceProperties;
    @SerializedName("targetType")
    @Expose
    private String targetType;

    public RMNCHAMotherInfantDetailsRequest(String uuid) {
        this.relationshipType = "MOTHEROF";
        this.sourceType = "Citizen";
        this.sourceProperties = new SourceProperties(uuid);
        this.targetType = "Citizen";
    }

    public class SourceProperties {

        @SerializedName("uuid")
        @Expose
        private String uuid;

        public SourceProperties(String uuid) {
            this.uuid = uuid;
        }
    }

}
