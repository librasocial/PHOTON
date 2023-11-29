package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RMNCHAPNCWomenRequest {

    @SerializedName("relationshipType")
    @Expose
    private String relationshipType;
    @SerializedName("sourceType")
    @Expose
    private String sourceType;
    @SerializedName("srcInClause")
    @Expose
    private SrcInClause srcInClause;
    @SerializedName("targetProperties")
    @Expose
    private TargetProperties targetProperties;

    @SerializedName("sourceProperties")
    @Expose
    private HashMap<String,Object> sourceProperties;
    @SerializedName("targetType")
    @Expose
    private String targetType;
    @SerializedName("stepCount")
    @Expose
    private int stepCount;

    public RMNCHAPNCWomenRequest(String uuid, String targetType) {
        this.relationshipType = "RESIDESIN";
        this.sourceType = "Citizen";
        this.targetProperties = new TargetProperties(uuid);
        this.targetType = targetType;
        this.sourceProperties= new HashMap<String,Object>(){{
            put("isAdult",true);
        }};
        this.stepCount = 2;
        this.srcInClause = new SrcInClause();
    }

    public class TargetProperties {
        @SerializedName("uuid")
        @Expose
        private String uuid;

        public TargetProperties(String uuid) {
            this.uuid = uuid;
        }
    }

    public class SrcInClause {
        @SerializedName("rmnchaServiceStatus")
        @Expose
        private List<String> rmnchaServiceStatus;

        public SrcInClause() {
            this.rmnchaServiceStatus = new ArrayList<>();
            rmnchaServiceStatus.add(String.valueOf(RMNCHAServiceStatus.PNC_OUTCOME_REGISTERED));
            rmnchaServiceStatus.add(String.valueOf(RMNCHAServiceStatus.PNC_INFANT_REGISTERED));
            rmnchaServiceStatus.add(String.valueOf(RMNCHAServiceStatus.PNC_ONGOING));
            rmnchaServiceStatus.add(String.valueOf(RMNCHAServiceStatus.PNCOngoing));
            rmnchaServiceStatus.add(String.valueOf(RMNCHAServiceStatus.ANC_ONGOING));
        }
    }

}
