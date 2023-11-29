package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAPNCMotherVisitLogResponse {


    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("rchId")
    @Expose
    public String rchId;
    @SerializedName("serviceId")
    @Expose
    public String serviceId;
    @SerializedName("pncPeriod")
    @Expose
    public String pncPeriod;
    @SerializedName("pncDate")
    @Expose
    public String pncDate;
    @SerializedName("noIFATabletsGiven")
    @Expose
    public String noIFATabletsGiven;
    @SerializedName("ppcMethod")
    @Expose
    public String ppcMethod;
    @SerializedName("signOfMotherDanger")
    @Expose
    public String signOfMotherDanger;
    @SerializedName("referralFacility")
    @Expose
    public String referralFacility;
    @SerializedName("referralPlace")
    @Expose
    public String referralPlace;
    @SerializedName("remark")
    @Expose
    public String remark;
    @SerializedName("isCovidTestDone")
    @Expose
    public boolean isCovidTestDone;
    @SerializedName("isCovidResultPositive")
    @Expose
    public boolean isCovidResultPositive;
    @SerializedName("isILIExperienced")
    @Expose
    public boolean isILIExperienced;
    @SerializedName("didContactCovidPatient")
    @Expose
    public boolean didContactCovidPatient;
    @SerializedName("complaints")
    @Expose
    public String complaints;
    @SerializedName("pallor")
    @Expose
    public String pallor;
    @SerializedName("bloodPressure")
    @Expose
    public String bloodPressure;
    @SerializedName("temperature")
    @Expose
    public String temperature;
    @SerializedName("breastsCondition")
    @Expose
    public String breastsCondition;
    @SerializedName("nippleCondition")
    @Expose
    public String nippleCondition;
    @SerializedName("uterusTenderness")
    @Expose
    public String uterusTenderness;
    @SerializedName("bleeding")
    @Expose
    public String bleeding;
    @SerializedName("lochiaCondition")
    @Expose
    public String lochiaCondition;
    @SerializedName("episiotomyORTear")
    @Expose
    public String episiotomyORTear;
    @SerializedName("familyPlanCounselling")
    @Expose
    public String familyPlanCounselling;
    @SerializedName("complications")
    @Expose
    public String complications;
    @SerializedName("isReferredToPHC")
    @Expose
    public boolean isReferredToPHC;
    @SerializedName("audit")
    @Expose
    public RMNCHAANCVisitLogResponse.Audit audit;

    public RMNCHAANCVisitLogResponse.Audit getAudit() {
        return audit;
    }

    public void setAudit(RMNCHAANCVisitLogResponse.Audit audit) {
        this.audit = audit;
    }

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

    public String getPncPeriod() {
        return pncPeriod;
    }

    public void setPncPeriod(String pncPeriod) {
        this.pncPeriod = pncPeriod;
    }

    public String getPncDate() {
        return pncDate;
    }

    public void setPncDate(String pncDate) {
        this.pncDate = pncDate;
    }

    public String getNoIFATabletsGiven() {
        return noIFATabletsGiven;
    }

    public void setNoIFATabletsGiven(String noIFATabletsGiven) {
        this.noIFATabletsGiven = noIFATabletsGiven;
    }

    public String getPpcMethod() {
        return ppcMethod;
    }

    public void setPpcMethod(String ppcMethod) {
        this.ppcMethod = ppcMethod;
    }

    public String getSignOfMotherDanger() {
        return signOfMotherDanger;
    }

    public void setSignOfMotherDanger(String signOfMotherDanger) {
        this.signOfMotherDanger = signOfMotherDanger;
    }

    public String getReferralFacility() {
        return referralFacility;
    }

    public void setReferralFacility(String referralFacility) {
        this.referralFacility = referralFacility;
    }

    public String getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(String referralPlace) {
        this.referralPlace = referralPlace;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isCovidTestDone() {
        return isCovidTestDone;
    }

    public void setCovidTestDone(boolean covidTestDone) {
        isCovidTestDone = covidTestDone;
    }

    public boolean isCovidResultPositive() {
        return isCovidResultPositive;
    }

    public void setCovidResultPositive(boolean covidResultPositive) {
        isCovidResultPositive = covidResultPositive;
    }

    public boolean isILIExperienced() {
        return isILIExperienced;
    }

    public void setILIExperienced(boolean ILIExperienced) {
        isILIExperienced = ILIExperienced;
    }

    public boolean isDidContactCovidPatient() {
        return didContactCovidPatient;
    }

    public void setDidContactCovidPatient(boolean didContactCovidPatient) {
        this.didContactCovidPatient = didContactCovidPatient;
    }

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getPallor() {
        return pallor;
    }

    public void setPallor(String pallor) {
        this.pallor = pallor;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBreastsCondition() {
        return breastsCondition;
    }

    public void setBreastsCondition(String breastsCondition) {
        this.breastsCondition = breastsCondition;
    }

    public String getNippleCondition() {
        return nippleCondition;
    }

    public void setNippleCondition(String nippleCondition) {
        this.nippleCondition = nippleCondition;
    }

    public String getUterusTenderness() {
        return uterusTenderness;
    }

    public void setUterusTenderness(String uterusTenderness) {
        this.uterusTenderness = uterusTenderness;
    }

    public String getBleeding() {
        return bleeding;
    }

    public void setBleeding(String bleeding) {
        this.bleeding = bleeding;
    }

    public String getLochiaCondition() {
        return lochiaCondition;
    }

    public void setLochiaCondition(String lochiaCondition) {
        this.lochiaCondition = lochiaCondition;
    }

    public String getEpisiotomyORTear() {
        return episiotomyORTear;
    }

    public void setEpisiotomyORTear(String episiotomyORTear) {
        this.episiotomyORTear = episiotomyORTear;
    }

    public String getFamilyPlanCounselling() {
        return familyPlanCounselling;
    }

    public void setFamilyPlanCounselling(String familyPlanCounselling) {
        this.familyPlanCounselling = familyPlanCounselling;
    }

    public String getComplications() {
        return complications;
    }

    public void setComplications(String complications) {
        this.complications = complications;
    }

    public boolean isReferredToPHC() {
        return isReferredToPHC;
    }

    public void setReferredToPHC(boolean referredToPHC) {
        isReferredToPHC = referredToPHC;
    }



    public RMNCHAPNCMotherVisitLogResponse() {

    }

    public static class Audit {
        @SerializedName("createdBy")
        @Expose
        public String createdBy;
        @SerializedName("modifiedBy")
        @Expose
        public String modifiedBy;
        @SerializedName("dateCreated")
        @Expose
        public String dateCreated;
        @SerializedName("dateModified")
        @Expose
        public String dateModified;

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getDateModified() {
            return dateModified;
        }

        public void setDateModified(String dateModified) {
            this.dateModified = dateModified;
        }
    }

}
