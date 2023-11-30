package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RMNCHAANCVisitLogRequest {

    @SerializedName("rchId")
    @Expose
    public String rchId;
    @SerializedName("serviceId")
    @Expose
    public String serviceId;
    @SerializedName("visitDate")
    @Expose
    public String visitDate;
    @SerializedName("financialYear")
    @Expose
    private String financialYear;
    @SerializedName("ashaWorker")
    @Expose
    private String ashaWorker;
    @SerializedName("ancFacilityName")
    @Expose
    private String ancFacilityName;
    @SerializedName("ancFacilityType")
    @Expose
    private String ancFacilityType;
    @SerializedName("weeksOfPregnancy")
    @Expose
    public int weeksOfPregnancy;
    @SerializedName("weight")
    @Expose
    public int weight;
    @SerializedName("midArmCircumference")
    @Expose
    public int midArmCircumference;
    @SerializedName("bpSystolic")
    @Expose
    public int bpSystolic;
    @SerializedName("bpDiastolic")
    @Expose
    public int bpDiastolic;
    @SerializedName("hb")
    @Expose
    public int hb;
    @SerializedName("isUrineTestDone")
    @Expose
    public boolean isUrineTestDone;
    @SerializedName("isUrineAlbuminPresent")
    @Expose
    public boolean isUrineAlbuminPresent;
    @SerializedName("isUrineSugarPresent")
    @Expose
    public boolean isUrineSugarPresent;
    @SerializedName("isBloodSugarTestDone")
    @Expose
    public boolean isBloodSugarTestDone;
    @SerializedName("fastingSugar")
    @Expose
    public int fastingSugar;
    @SerializedName("postPrandialSugar")
    @Expose
    public int postPrandialSugar;
    @SerializedName("randomSugar")
    @Expose
    public int randomSugar;
    @SerializedName("tshLevel")
    @Expose
    public int tshLevel;
    @SerializedName("gtt")
    @Expose
    public int gtt;
    @SerializedName("ogtt")
    @Expose
    public int ogtt;
    @SerializedName("ttDose")
    @Expose
    public String ttDose;
    @SerializedName("ttDoseTakenDate")
    @Expose
    public String ttDoseTakenDate;
    @SerializedName("noFATabletsGiven")
    @Expose
    public int noFATabletsGiven;
    @SerializedName("noIFATabletsGiven")
    @Expose
    public int noIFATabletsGiven;
    @SerializedName("fundalHeightByUterusSizeRatio")
    @Expose
    public int fundalHeightByUterusSizeRatio;
    @SerializedName("foetalHeartRatePerMin")
    @Expose
    public int foetalHeartRatePerMin;
    @SerializedName("foetalPosition")
    @Expose
    public String foetalPosition;
    @SerializedName("foetalMovements")
    @Expose
    public String foetalMovements;
    @SerializedName("highRiskSymptoms")
    @Expose
    public List<String> highRiskSymptoms;
    @SerializedName("referralFacilityType")
    @Expose
    public String referralFacilityType;
    @SerializedName("referralFacility")
    @Expose
    public String referralFacility;
    @SerializedName("hasDeliveredChild")
    @Expose
    public boolean hasDeliveredChild;
    @SerializedName("deliverDate")
    @Expose
    public String deliverDate;
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
    @SerializedName("hasAborted")
    @Expose
    public boolean hasAborted;
    @SerializedName("hasMaternalDealth")
    @Expose
    public boolean hasMaternalDealth;

    public String getAshaWorker() {
        return ashaWorker;
    }

    public void setAshaWorker(String ashaWorker) {
        this.ashaWorker = ashaWorker;
    }

    public boolean isHasDeliveredChild() {
        return hasDeliveredChild;
    }

    public void setHasDeliveredChild(boolean hasDeliveredChild) {
        this.hasDeliveredChild = hasDeliveredChild;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
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

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public int getWeeksOfPregnancy() {
        return weeksOfPregnancy;
    }

    public void setWeeksOfPregnancy(int weeksOfPregnancy) {
        this.weeksOfPregnancy = weeksOfPregnancy;
    }

    public String getAncFacilityName() {
        return ancFacilityName;
    }

    public void setAncFacilityName(String ancFacilityName) {
        this.ancFacilityName = ancFacilityName;
    }

    public String getAncFacilityType() {
        return ancFacilityType;
    }

    public void setAncFacilityType(String ancFacilityType) {
        this.ancFacilityType = ancFacilityType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMidArmCircumference() {
        return midArmCircumference;
    }

    public void setMidArmCircumference(int midArmCircumference) {
        this.midArmCircumference = midArmCircumference;
    }

    public int getBpSystolic() {
        return bpSystolic;
    }

    public void setBpSystolic(int bpSystolic) {
        this.bpSystolic = bpSystolic;
    }

    public int getBpDiastolic() {
        return bpDiastolic;
    }

    public void setBpDiastolic(int bpDiastolic) {
        this.bpDiastolic = bpDiastolic;
    }

    public int getHb() {
        return hb;
    }

    public void setHb(int hb) {
        this.hb = hb;
    }

    public boolean isUrineTestDone() {
        return isUrineTestDone;
    }

    public void setUrineTestDone(boolean urineTestDone) {
        isUrineTestDone = urineTestDone;
    }

    public boolean isUrineAlbuminPresent() {
        return isUrineAlbuminPresent;
    }

    public void setUrineAlbuminPresent(boolean urineAlbuminPresent) {
        isUrineAlbuminPresent = urineAlbuminPresent;
    }

    public boolean isUrineSugarPresent() {
        return isUrineSugarPresent;
    }

    public void setUrineSugarPresent(boolean urineSugarPresent) {
        isUrineSugarPresent = urineSugarPresent;
    }

    public boolean isBloodSugarTestDone() {
        return isBloodSugarTestDone;
    }

    public void setBloodSugarTestDone(boolean bloodSugarTestDone) {
        isBloodSugarTestDone = bloodSugarTestDone;
    }

    public int getFastingSugar() {
        return fastingSugar;
    }

    public void setFastingSugar(int fastingSugar) {
        this.fastingSugar = fastingSugar;
    }

    public int getPostPrandialSugar() {
        return postPrandialSugar;
    }

    public void setPostPrandialSugar(int postPrandialSugar) {
        this.postPrandialSugar = postPrandialSugar;
    }

    public int getRandomSugar() {
        return randomSugar;
    }

    public void setRandomSugar(int randomSugar) {
        this.randomSugar = randomSugar;
    }

    public int getTshLevel() {
        return tshLevel;
    }

    public void setTshLevel(int tshLevel) {
        this.tshLevel = tshLevel;
    }

    public int getGtt() {
        return gtt;
    }

    public void setGtt(int gtt) {
        this.gtt = gtt;
    }

    public int getOgtt() {
        return ogtt;
    }

    public void setOgtt(int ogtt) {
        this.ogtt = ogtt;
    }

    public String getTtDose() {
        return ttDose;
    }

    public void setTtDose(String ttDose) {
        this.ttDose = ttDose;
    }

    public String getTtDoseTakenDate() {
        return ttDoseTakenDate;
    }

    public void setTtDoseTakenDate(String ttDoseTakenDate) {
        this.ttDoseTakenDate = ttDoseTakenDate;
    }

    public int getNoFATabletsGiven() {
        return noFATabletsGiven;
    }

    public void setNoFATabletsGiven(int noFATabletsGiven) {
        this.noFATabletsGiven = noFATabletsGiven;
    }

    public int getNoIFATabletsGiven() {
        return noIFATabletsGiven;
    }

    public void setNoIFATabletsGiven(int noIFATabletsGiven) {
        this.noIFATabletsGiven = noIFATabletsGiven;
    }

    public int getFundalHeightByUterusSizeRatio() {
        return fundalHeightByUterusSizeRatio;
    }

    public void setFundalHeightByUterusSizeRatio(int fundalHeightByUterusSizeRatio) {
        this.fundalHeightByUterusSizeRatio = fundalHeightByUterusSizeRatio;
    }

    public int getFoetalHeartRatePerMin() {
        return foetalHeartRatePerMin;
    }

    public void setFoetalHeartRatePerMin(int foetalHeartRatePerMin) {
        this.foetalHeartRatePerMin = foetalHeartRatePerMin;
    }

    public String getFoetalPosition() {
        return foetalPosition;
    }

    public void setFoetalPosition(String foetalPosition) {
        this.foetalPosition = foetalPosition;
    }

    public String getFoetalMovements() {
        return foetalMovements;
    }

    public void setFoetalMovements(String foetalMovements) {
        this.foetalMovements = foetalMovements;
    }

    public List<String> getHighRiskSymptoms() {
        return highRiskSymptoms;
    }

    public void setHighRiskSymptoms(List<String> highRiskSymptoms) {
        this.highRiskSymptoms = highRiskSymptoms;
    }

    public String getReferralFacilityType() {
        return referralFacilityType;
    }

    public void setReferralFacilityType(String referralFacilityType) {
        this.referralFacilityType = referralFacilityType;
    }

    public String getReferralFacility() {
        return referralFacility;
    }

    public void setReferralFacility(String referralFacility) {
        this.referralFacility = referralFacility;
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

    public boolean isHasAborted() {
        return hasAborted;
    }

    public void setHasAborted(boolean hasAborted) {
        this.hasAborted = hasAborted;
    }

    public boolean isHasMaternalDealth() {
        return hasMaternalDealth;
    }

    public void setHasMaternalDealth(boolean hasMaternalDealth) {
        this.hasMaternalDealth = hasMaternalDealth;
    }

    public RMNCHAANCVisitLogRequest() {

    }

}
