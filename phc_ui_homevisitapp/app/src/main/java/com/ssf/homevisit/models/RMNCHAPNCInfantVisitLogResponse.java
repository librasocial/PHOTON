package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAPNCInfantVisitLogResponse {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("childId")
    @Expose
    public String childId;
    @SerializedName("serviceId")
    @Expose
    public String serviceId;
    @SerializedName("pncPeriod")
    @Expose
    public String pncPeriod;
    @SerializedName("pncDate")
    @Expose
    public String pncDate;
    @SerializedName("signOfInfantDanger")
    @Expose
    public String signOfInfantDanger;
    @SerializedName("referralFacility")
    @Expose
    public String referralFacility;
    @SerializedName("referralPlace")
    @Expose
    public String referralPlace;
    @SerializedName("weight")
    @Expose
    public String weight;
    @SerializedName("hasInfantDeath")
    @Expose
    public boolean hasInfantDeath;
    @SerializedName("urinePassed")
    @Expose
    public String urinePassed;
    @SerializedName("stoolPassed")
    @Expose
    public String stoolPassed;
    @SerializedName("temperature")
    @Expose
    public String temperature;
    @SerializedName("isILIExperienced")
    @Expose
    public boolean isILIExperienced;
    @SerializedName("jaundice")
    @Expose
    public String jaundice;
    @SerializedName("diarrhea")
    @Expose
    public String diarrhea;
    @SerializedName("vomiting")
    @Expose
    public String vomiting;
    @SerializedName("convulsions")
    @Expose
    public String convulsions;
    @SerializedName("activity")
    @Expose
    public String activity;
    @SerializedName("sucking")
    @Expose
    public String sucking;
    @SerializedName("breathing")
    @Expose
    public String breathing;
    @SerializedName("chestDrawing")
    @Expose
    public String chestDrawing;
    @SerializedName("skinPustules")
    @Expose
    public String skinPustules;
    @SerializedName("umbilicalStumpCondition")
    @Expose
    public String umbilicalStumpCondition;
    @SerializedName("complications")
    @Expose
    public String complications;
    @SerializedName("audit")
    @Expose
    public RMNCHAANCVisitLogResponse.Audit audit;

    public RMNCHAANCVisitLogResponse.Audit getAudit() {
        return audit;
    }

    public void setAudit(RMNCHAANCVisitLogResponse.Audit audit) {
        this.audit = audit;
    }

    public boolean isHasInfantDeath() {
        return hasInfantDeath;
    }

    public void setHasInfantDeath(boolean hasInfantDeath) {
        this.hasInfantDeath = hasInfantDeath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
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

    public String getSignOfInfantDanger() {
        return signOfInfantDanger;
    }

    public void setSignOfInfantDanger(String signOfInfantDanger) {
        this.signOfInfantDanger = signOfInfantDanger;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUrinePassed() {
        return urinePassed;
    }

    public void setUrinePassed(String urinePassed) {
        this.urinePassed = urinePassed;
    }

    public String getStoolPassed() {
        return stoolPassed;
    }

    public void setStoolPassed(String stoolPassed) {
        this.stoolPassed = stoolPassed;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public boolean isILIExperienced() {
        return isILIExperienced;
    }

    public void setILIExperienced(boolean ILIExperienced) {
        isILIExperienced = ILIExperienced;
    }

    public String getJaundice() {
        return jaundice;
    }

    public void setJaundice(String jaundice) {
        this.jaundice = jaundice;
    }

    public String getDiarrhea() {
        return diarrhea;
    }

    public void setDiarrhea(String diarrhea) {
        this.diarrhea = diarrhea;
    }

    public String getVomiting() {
        return vomiting;
    }

    public void setVomiting(String vomiting) {
        this.vomiting = vomiting;
    }

    public String getConvulsions() {
        return convulsions;
    }

    public void setConvulsions(String convulsions) {
        this.convulsions = convulsions;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSucking() {
        return sucking;
    }

    public void setSucking(String sucking) {
        this.sucking = sucking;
    }

    public String getBreathing() {
        return breathing;
    }

    public void setBreathing(String breathing) {
        this.breathing = breathing;
    }

    public String getChestDrawing() {
        return chestDrawing;
    }

    public void setChestDrawing(String chestDrawing) {
        this.chestDrawing = chestDrawing;
    }

    public String getSkinPustules() {
        return skinPustules;
    }

    public void setSkinPustules(String skinPustules) {
        this.skinPustules = skinPustules;
    }

    public String getUmbilicalStumpCondition() {
        return umbilicalStumpCondition;
    }

    public void setUmbilicalStumpCondition(String umbilicalStumpCondition) {
        this.umbilicalStumpCondition = umbilicalStumpCondition;
    }

    public String getComplications() {
        return complications;
    }

    public void setComplications(String complications) {
        this.complications = complications;
    }

    public RMNCHAPNCInfantVisitLogResponse() {

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
