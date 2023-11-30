package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHACreateECServiceRequest {

    @SerializedName("ashaWorker")
    @Expose
    private String ashaWorker;
    @SerializedName("rchId")
    @Expose
    private String rchId;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("visitDate")
    @Expose
    private String visitDate;
    @SerializedName("financialYear")
    @Expose
    private String financialYear;
    @SerializedName("isReferredToPHC")
    @Expose
    private boolean isReferredToPHC;
    @SerializedName("bcServiceOfferedTo")
    @Expose
    private String bcServiceOfferedTo;
    @SerializedName("bcTypeOfContraceptive")
    @Expose
    private String bcTypeOfContraceptive;
    @SerializedName("bcContraceptiveQuantity")
    @Expose
    private String bcContraceptiveQuantity;
    @SerializedName("pcServiceList")
    @Expose
    private String[] pcServiceList;
    @SerializedName("pcHasStoppedContraceptive")
    @Expose
    private boolean pcHasStoppedContraceptive;
    @SerializedName("isPregnancyTestTaken")
    @Expose
    private boolean isPregnancyTestTaken;
    @SerializedName("pregnancyTestResult")
    @Expose
    private String pregnancyTestResult;
    @SerializedName("couple")
    @Expose
    private Couple couple;
    @SerializedName("audit")
    @Expose
    private Audit audit;

    public RMNCHACreateECServiceRequest(String ashaWorker, String rchId, String serviceType,
                                        String visitDate, String financialYear,
                                        boolean isReferredToPHC, String bcServiceOfferedTo,
                                        String bcTypeOfContraceptive, String bcContraceptiveQuantity,
                                        String pregnantTestResult,
                                        String wifeID, String husbandID) {
        this.ashaWorker = ashaWorker;
        this.rchId = rchId;
        this.serviceType = serviceType;
        this.visitDate = visitDate;
        this.financialYear = financialYear;
        this.isReferredToPHC = isReferredToPHC;
        this.bcServiceOfferedTo = bcServiceOfferedTo;
        this.bcTypeOfContraceptive = bcTypeOfContraceptive;
        this.bcContraceptiveQuantity = bcContraceptiveQuantity;
        this.pregnancyTestResult = pregnantTestResult;
        this.couple = new Couple(wifeID, husbandID);
    }

    public RMNCHACreateECServiceRequest(String ashaWorker, String rchId, String serviceType,
                                        String visitDate, String financialYear,
                                        String[] pcServiceList, boolean pcHasStoppedContraceptive,
                                        boolean isPregnancyTestTaken, String pregnancyTestResult,
                                        String wifeID, String husbandID) {
        this.ashaWorker = ashaWorker;
        this.rchId = rchId;
        this.serviceType = serviceType;
        this.visitDate = visitDate;
        this.financialYear = financialYear;
        this.pcServiceList = pcServiceList;
        this.pcHasStoppedContraceptive = pcHasStoppedContraceptive;
        this.isPregnancyTestTaken = isPregnancyTestTaken;
        this.pregnancyTestResult = pregnancyTestResult;
        this.couple = new Couple(wifeID, husbandID);
    }

    public class Couple {
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
    }

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
