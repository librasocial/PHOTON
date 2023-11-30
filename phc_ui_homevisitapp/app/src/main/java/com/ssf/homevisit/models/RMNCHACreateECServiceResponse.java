package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHACreateECServiceResponse {

    @SerializedName("id")
    @Expose
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAshaWorker() {
        return ashaWorker;
    }

    public void setAshaWorker(String ashaWorker) {
        this.ashaWorker = ashaWorker;
    }

    public String getRchId() {
        return rchId;
    }

    public void setRchId(String rchId) {
        this.rchId = rchId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public boolean isReferredToPHC() {
        return isReferredToPHC;
    }

    public void setReferredToPHC(boolean referredToPHC) {
        isReferredToPHC = referredToPHC;
    }

    public String getBcServiceOfferedTo() {
        return bcServiceOfferedTo;
    }

    public void setBcServiceOfferedTo(String bcServiceOfferedTo) {
        this.bcServiceOfferedTo = bcServiceOfferedTo;
    }

    public String getBcTypeOfContraceptive() {
        return bcTypeOfContraceptive;
    }

    public void setBcTypeOfContraceptive(String bcTypeOfContraceptive) {
        this.bcTypeOfContraceptive = bcTypeOfContraceptive;
    }

    public String getBcContraceptiveQuantity() {
        return bcContraceptiveQuantity;
    }

    public void setBcContraceptiveQuantity(String bcContraceptiveQuantity) {
        this.bcContraceptiveQuantity = bcContraceptiveQuantity;
    }

    public String[] getPcServiceList() {
        return pcServiceList;
    }

    public void setPcServiceList(String[] pcServiceList) {
        this.pcServiceList = pcServiceList;
    }

    public boolean isPcHasStoppedContraceptive() {
        return pcHasStoppedContraceptive;
    }

    public void setPcHasStoppedContraceptive(boolean pcHasStoppedContraceptive) {
        this.pcHasStoppedContraceptive = pcHasStoppedContraceptive;
    }

    public boolean isPregnancyTestTaken() {
        return isPregnancyTestTaken;
    }

    public void setPregnancyTestTaken(boolean pregnancyTestTaken) {
        isPregnancyTestTaken = pregnancyTestTaken;
    }

    public String getPregnancyTestResult() {
        return pregnancyTestResult;
    }

    public void setPregnancyTestResult(String pregnancyTestResult) {
        this.pregnancyTestResult = pregnancyTestResult;
    }

    public Couple getCouple() {
        return couple;
    }

    public void setCouple(Couple couple) {
        this.couple = couple;
    }

    public class Couple {
        @SerializedName("wifeId")
        @Expose
        private String wifeId;
        @SerializedName("husbandId")
        @Expose
        private String husbandId;

        public String getWifeId() {
            return wifeId;
        }

        public void setWifeId(String wifeId) {
            this.wifeId = wifeId;
        }

        public String getHusbandId() {
            return husbandId;
        }

        public void setHusbandId(String husbandId) {
            this.husbandId = husbandId;
        }
    }


}
