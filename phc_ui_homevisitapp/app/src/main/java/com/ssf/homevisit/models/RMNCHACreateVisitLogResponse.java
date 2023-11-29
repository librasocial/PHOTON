package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHACreateVisitLogResponse {

    /* @SerializedName("ashaWorker")
     @Expose
     private String ashaWorker;*/
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rchId")
    @Expose
    private String rchId;
    @SerializedName("serviceId")
    @Expose
    private String serviceId;
    /*@SerializedName("serviceType")
    @Expose
    private String serviceType;*/
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
    @SerializedName("pcContraceptiveStopDate")
    @Expose
    private String pcContraceptiveStopDate;
    @SerializedName("pregnancyTestTaken")
    @Expose
    private boolean pregnancyTestTaken;
    @SerializedName("pregnancyTestResult")
    @Expose
    private String pregnancyTestResult;
    /* @SerializedName("couple")
     @Expose
     private Couple couple;*/
    @SerializedName("audit")
    @Expose
    private Audit audit;

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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public boolean isReferredToPHC() {
        return referredToPHC;
    }

    public void setReferredToPHC(boolean referredToPHC) {
        this.referredToPHC = referredToPHC;
    }

    public String getBcOcpType() {
        return bcOcpType;
    }

    public void setBcOcpType(String bcOcpType) {
        this.bcOcpType = bcOcpType;
    }

    public String getBcQuantity() {
        return bcQuantity;
    }

    public void setBcQuantity(String bcQuantity) {
        this.bcQuantity = bcQuantity;
    }

    public String getPcContraceptiveStopDate() {
        return pcContraceptiveStopDate;
    }

    public void setPcContraceptiveStopDate(String pcContraceptiveStopDate) {
        this.pcContraceptiveStopDate = pcContraceptiveStopDate;
    }

    public boolean isPregnancyTestTaken() {
        return pregnancyTestTaken;
    }

    public void setPregnancyTestTaken(boolean pregnancyTestTaken) {
        this.pregnancyTestTaken = pregnancyTestTaken;
    }

    public String getPregnancyTestResult() {
        return pregnancyTestResult;
    }

    public void setPregnancyTestResult(String pregnancyTestResult) {
        this.pregnancyTestResult = pregnancyTestResult;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
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

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getDateModified() {
            return dateModified;
        }

        public void setDateModified(String dateModified) {
            this.dateModified = dateModified;
        }
    }

}
