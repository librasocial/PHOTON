package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHACreateVisitLogRequest {

    /* @SerializedName("ashaWorker")
     @Expose
     private String ashaWorker;*/
    @SerializedName("rchId")
    @Expose
    private String rchId;
    @SerializedName("serviceId")
    @Expose
    private String serviceId;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("visitDate")
    @Expose
    private String visitDate;
    /*@SerializedName("financialYear")
    @Expose
    private String financialYear;*/
    @SerializedName("referredToPHC")
    @Expose
    private boolean referredToPHC;
    /* @SerializedName("bcServiceOfferedTo")
     @Expose
     private String bcServiceOfferedTo;*/
    @SerializedName("bcOcpType")
    @Expose
    private String bcOcpType;
    @SerializedName("bcQuantity")
    @Expose
    private String bcQuantity;
    /*@SerializedName("pcServiceList")
    @Expose
    private String[] pcServiceList;*/
    /*@SerializedName("pcHasStoppedContraceptive")
    @Expose
    private boolean pcHasStoppedContraceptive;*/
   /* @SerializedName("pcContraceptiveStopDate")
    @Expose
    private String pcContraceptiveStopDate;*/
    @SerializedName("isPregnancyTestTaken")
    @Expose
    private boolean isPregnancyTestTaken;
    @SerializedName("pregnancyTestResult")
    @Expose
    private String pregnancyTestResult;
    /* @SerializedName("couple")
     @Expose
     private Couple couple;*/
    @SerializedName("audit")
    @Expose
    private Audit audit;

    public RMNCHACreateVisitLogRequest(String rchId, String serviceId,String serviceType, String visitDate,
                                       boolean isReferredToPHC, String bcTypeOfContraceptive,
                                       String bcContraceptiveQuantity,  boolean isPregnancyTestTaken,
                                       String pregnancyTestResult, Audit audit) {
        //this.ashaWorker = ashaWorker;
        this.rchId = rchId;
        this.serviceType = serviceType;
        this.visitDate = visitDate;
        //this.financialYear = financialYear;
        this.isPregnancyTestTaken = isPregnancyTestTaken;
        this.pregnancyTestResult = pregnancyTestResult;
        this.referredToPHC = isReferredToPHC;
        this.bcOcpType = bcTypeOfContraceptive;
        this.bcQuantity = bcContraceptiveQuantity;
        //this.pcContraceptiveStopDate = pcContraceptiveStopDate;
        //this.bcServiceOfferedTo = bcServiceOfferedTo;
        //this.pcServiceList = pcServiceList;
        //this.pcHasStoppedContraceptive = pcHasStoppedContraceptive;
        //this.couple = couple;
        this.audit = audit;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /*public class Couple {
        @SerializedName("wifeId")
        @Expose
        private String wifeId;
        @SerializedName("husbandId")
        @Expose
        private String husbandId;

        public Couple(String wifeId, String husbandId) {
            this.wifeId = wifeId;
            this.husbandId = husbandId;
        }
    }*/

    public class Audit {
        @SerializedName("createdBy")
        @Expose
        private String createdBy;
        @SerializedName("dateCreated")
        @Expose
        private String dateCreated;
        @SerializedName("modifiedBy")
        @Expose
        private String modifiedBy;
        @SerializedName("dateModified")
        @Expose
        private String dateModified;

        public Audit(String createdBy, String dateCreated, String modifiedBy, String dateModified) {
            this.createdBy = createdBy;
            this.dateCreated = dateCreated;
            this.modifiedBy = modifiedBy;
            this.dateModified = dateModified;
        }
    }

}
