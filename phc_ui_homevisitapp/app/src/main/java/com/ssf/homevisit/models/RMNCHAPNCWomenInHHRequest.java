package com.ssf.homevisit.models;

import com.google.api.BackendOrBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashMap;

public class RMNCHAPNCWomenInHHRequest {

    @SerializedName("relationshipType")
    @Expose
    private String relationshipType;
    @SerializedName("sourceType")
    @Expose
    private String sourceType;

    @SerializedName("srcInClause")
    @Expose
    private HashMap<String, Object> srcInClause;
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

    public RMNCHAPNCWomenInHHRequest(String uuid) {
        this.relationshipType = "RESIDESIN";
        this.sourceType = "Citizen";
        this.sourceProperties = new SourceProperties();
        this.targetProperties = new TargetProperties(uuid);
        this.targetType = "HouseHold";
        this.stepCount = 1;
        this.srcInClause = new HashMap<String, Object>() {{
            put("rmnchaServiceStatus", Arrays.asList("ANC_ONGOING", "PNC_ONGOING",
                    "PNC_INFANT_REGISTERED",
                    "PNC_OUTCOME_REGISTERED"));
        }};
    }

    public class SourceProperties {
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("isAdult")
        @Expose
        private Boolean isAdult;

        public SourceProperties() {
            this.gender = "Female";
            this.isAdult=true;
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
