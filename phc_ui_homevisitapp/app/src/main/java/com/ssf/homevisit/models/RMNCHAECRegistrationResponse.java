package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RMNCHAECRegistrationResponse {

    @SerializedName("couple")
    @Expose
    private Couple couple;
    @SerializedName("coupleAdditionalDetails")
    @Expose
    private CoupleAdditionalDetails coupleAdditionalDetails;
    @SerializedName("rchGeneration")
    @Expose
    private RchGeneration rchGeneration;

    public Couple getCouple() {
        return couple;
    }

    public void setCouple(Couple couple) {
        this.couple = couple;
    }

    public CoupleAdditionalDetails getCoupleAdditionalDetails() {
        return coupleAdditionalDetails;
    }

    public void setCoupleAdditionalDetails(CoupleAdditionalDetails coupleAdditionalDetails) {
        this.coupleAdditionalDetails = coupleAdditionalDetails;
    }

    public RchGeneration getRchGeneration() {
        return rchGeneration;
    }

    public void setRchGeneration(RchGeneration rchGeneration) {
        this.rchGeneration = rchGeneration;
    }

    public RMNCHAECRegistrationResponse() {
    }

    public static class Couple {

        public Couple() {
        }

        @SerializedName("husbandId")
        @Expose
        private String husbandId;
        @SerializedName("husbandName")
        @Expose
        private String husbandName;
        @SerializedName("husbandPhone")
        @Expose
        private String husbandPhone;
        @SerializedName("husbandAge")
        @Expose
        private int husbandAge;
        @SerializedName("husbandDOB")
        @Expose
        private String husbandDOB;
        @SerializedName("husbandHealthId")
        @Expose
        private String husbandHealthId;
        @SerializedName("husbandAgeAtMarriage")
        @Expose
        private int husbandAgeAtMarriage;
        @SerializedName("wifeId")
        @Expose
        private String wifeId;
        @SerializedName("wifeName")
        @Expose
        private String wifeName;
        @SerializedName("wifePhone")
        @Expose
        private String wifePhone;
        @SerializedName("wifeAge")
        @Expose
        private int wifeAge;
        @SerializedName("wifeDOB")
        @Expose
        private String wifeDOB;
        @SerializedName("wifeHealthId")
        @Expose
        private String wifeHealthId;
        @SerializedName("wifeAgeAtMarriage")
        @Expose
        private int wifeAgeAtMarriage;
        @SerializedName("religion")
        @Expose
        private String religion;
        @SerializedName("caste")
        @Expose
        private String caste;
        @SerializedName("economicStatus")
        @Expose
        private String economicStatus;
        @SerializedName("totalMaleChildren")
        @Expose
        private int totalMaleChildren;
        @SerializedName("totalFemaleChildren")
        @Expose
        private int totalFemaleChildren;
        @SerializedName("totalMaleChildrenAlive")
        @Expose
        private int totalMaleChildrenAlive;
        @SerializedName("totalFemaleChildrenAlive")
        @Expose
        private int totalFemaleChildrenAlive;
        @SerializedName("infertilityStatus")
        @Expose
        private String infertilityStatus;
        @SerializedName("serialNumber")
        @Expose
        private int serialNumber;
        @SerializedName("registeredBy")
        @Expose
        private String registeredBy;
        @SerializedName("registeredByName")
        @Expose
        private String registeredByName;
        @SerializedName("registeredOn")
        @Expose
        private String registeredOn;

        public String getHusbandId() {
            return husbandId;
        }

        public void setHusbandId(String husbandId) {
            this.husbandId = husbandId;
        }

        public String getHusbandName() {
            return husbandName;
        }

        public void setHusbandName(String husbandName) {
            this.husbandName = husbandName;
        }

        public String getHusbandPhone() {
            return husbandPhone;
        }

        public void setHusbandPhone(String husbandPhone) {
            this.husbandPhone = husbandPhone;
        }

        public int getHusbandAge() {
            return husbandAge;
        }

        public void setHusbandAge(int husbandAge) {
            this.husbandAge = husbandAge;
        }

        public String getHusbandDOB() {
            return husbandDOB;
        }

        public void setHusbandDOB(String husbandDOB) {
            this.husbandDOB = husbandDOB;
        }

        public String getHusbandHealthId() {
            return husbandHealthId;
        }

        public void setHusbandHealthId(String husbandHealthId) {
            this.husbandHealthId = husbandHealthId;
        }

        public int getHusbandAgeAtMarriage() {
            return husbandAgeAtMarriage;
        }

        public void setHusbandAgeAtMarriage(int husbandAgeAtMarriage) {
            this.husbandAgeAtMarriage = husbandAgeAtMarriage;
        }

        public String getWifeId() {
            return wifeId;
        }

        public void setWifeId(String wifeId) {
            this.wifeId = wifeId;
        }

        public String getWifeName() {
            return wifeName;
        }

        public void setWifeName(String wifeName) {
            this.wifeName = wifeName;
        }

        public String getWifePhone() {
            return wifePhone;
        }

        public void setWifePhone(String wifePhone) {
            this.wifePhone = wifePhone;
        }

        public int getWifeAge() {
            return wifeAge;
        }

        public void setWifeAge(int wifeAge) {
            this.wifeAge = wifeAge;
        }

        public String getWifeDOB() {
            return wifeDOB;
        }

        public void setWifeDOB(String wifeDOB) {
            this.wifeDOB = wifeDOB;
        }

        public String getWifeHealthId() {
            return wifeHealthId;
        }

        public void setWifeHealthId(String wifeHealthId) {
            this.wifeHealthId = wifeHealthId;
        }

        public int getWifeAgeAtMarriage() {
            return wifeAgeAtMarriage;
        }

        public void setWifeAgeAtMarriage(int wifeAgeAtMarriage) {
            this.wifeAgeAtMarriage = wifeAgeAtMarriage;
        }

        public String getReligion() {
            return religion;
        }

        public void setReligion(String religion) {
            this.religion = religion;
        }

        public String getCaste() {
            return caste;
        }

        public void setCaste(String caste) {
            this.caste = caste;
        }

        public String getEconomicStatus() {
            return economicStatus;
        }

        public void setEconomicStatus(String economicStatus) {
            this.economicStatus = economicStatus;
        }

        public int getTotalMaleChildren() {
            return totalMaleChildren;
        }

        public void setTotalMaleChildren(int totalMaleChildren) {
            this.totalMaleChildren = totalMaleChildren;
        }

        public int getTotalFemaleChildren() {
            return totalFemaleChildren;
        }

        public void setTotalFemaleChildren(int totalFemaleChildren) {
            this.totalFemaleChildren = totalFemaleChildren;
        }

        public int getTotalMaleChildrenAlive() {
            return totalMaleChildrenAlive;
        }

        public void setTotalMaleChildrenAlive(int totalMaleChildrenAlive) {
            this.totalMaleChildrenAlive = totalMaleChildrenAlive;
        }

        public int getTotalFemaleChildrenAlive() {
            return totalFemaleChildrenAlive;
        }

        public void setTotalFemaleChildrenAlive(int totalFemaleChildrenAlive) {
            this.totalFemaleChildrenAlive = totalFemaleChildrenAlive;
        }

        public String getInfertilityStatus() {
            return infertilityStatus;
        }

        public void setInfertilityStatus(String infertilityStatus) {
            this.infertilityStatus = infertilityStatus;
        }

        public int getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(int serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getRegisteredBy() {
            return registeredBy;
        }

        public void setRegisteredBy(String registeredBy) {
            this.registeredBy = registeredBy;
        }

        public String getRegisteredByName() {
            return registeredByName;
        }

        public void setRegisteredByName(String registeredByName) {
            this.registeredByName = registeredByName;
        }

        public String getRegisteredOn() {
            return registeredOn;
        }

        public void setRegisteredOn(String registeredOn) {
            this.registeredOn = registeredOn;
        }
    }

    public static class CoupleAdditionalDetails {

        public CoupleAdditionalDetails() {
        }

        @SerializedName("rationCardNo")
        @Expose
        private String rationCardNo;
        @SerializedName("husbandAadharEnrollmentNo")
        @Expose
        private String husbandAadharEnrollmentNo;
        @SerializedName("husbandAadharNo")
        @Expose
        private String husbandAadharNo;
        @SerializedName("husbandBankAadharLinked")
        @Expose
        private boolean husbandBankAadharLinked;
        @SerializedName("husbandBankName")
        @Expose
        private String husbandBankName;
        @SerializedName("husbandBankBranch")
        @Expose
        private String husbandBankBranch;
        @SerializedName("husbandBankACNo")
        @Expose
        private String husbandBankACNo;
        @SerializedName("husbandBankIFSCCode")
        @Expose
        private String husbandBankIFSCCode;
        @SerializedName("wifeAadharEnrollmentNo")
        @Expose
        private String wifeAadharEnrollmentNo;
        @SerializedName("wifeAadharNo")
        @Expose
        private String wifeAadharNo;
        @SerializedName("wifeBankAadharLinked")
        @Expose
        private boolean wifeBankAadharLinked;
        @SerializedName("wifeBankName")
        @Expose
        private String wifeBankName;
        @SerializedName("wifeBankBranch")
        @Expose
        private String wifeBankBranch;
        @SerializedName("wifeBankACNo")
        @Expose
        private String wifeBankACNo;
        @SerializedName("wifeBankIFSCCode")
        @Expose
        private String wifeBankIFSCCode;
        @SerializedName("addedBy")
        @Expose
        private String addedBy;
        @SerializedName("addedOn")
        @Expose
        private String addedOn;

        public String getRationCardNo() {
            return rationCardNo;
        }

        public void setRationCardNo(String rationCardNo) {
            this.rationCardNo = rationCardNo;
        }

        public String getHusbandAadharEnrollmentNo() {
            return husbandAadharEnrollmentNo;
        }

        public void setHusbandAadharEnrollmentNo(String husbandAadharEnrollmentNo) {
            this.husbandAadharEnrollmentNo = husbandAadharEnrollmentNo;
        }

        public String getHusbandAadharNo() {
            return husbandAadharNo;
        }

        public void setHusbandAadharNo(String husbandAadharNo) {
            this.husbandAadharNo = husbandAadharNo;
        }

        public boolean isHusbandBankAadharLinked() {
            return husbandBankAadharLinked;
        }

        public void setHusbandBankAadharLinked(boolean husbandBankAadharLinked) {
            this.husbandBankAadharLinked = husbandBankAadharLinked;
        }

        public String getHusbandBankName() {
            return husbandBankName;
        }

        public void setHusbandBankName(String husbandBankName) {
            this.husbandBankName = husbandBankName;
        }

        public String getHusbandBankBranch() {
            return husbandBankBranch;
        }

        public void setHusbandBankBranch(String husbandBankBranch) {
            this.husbandBankBranch = husbandBankBranch;
        }

        public String getHusbandBankACNo() {
            return husbandBankACNo;
        }

        public void setHusbandBankACNo(String husbandBankACNo) {
            this.husbandBankACNo = husbandBankACNo;
        }

        public String getHusbandBankIFSCCode() {
            return husbandBankIFSCCode;
        }

        public void setHusbandBankIFSCCode(String husbandBankIFSCCode) {
            this.husbandBankIFSCCode = husbandBankIFSCCode;
        }

        public String getWifeAadharEnrollmentNo() {
            return wifeAadharEnrollmentNo;
        }

        public void setWifeAadharEnrollmentNo(String wifeAadharEnrollmentNo) {
            this.wifeAadharEnrollmentNo = wifeAadharEnrollmentNo;
        }

        public String getWifeAadharNo() {
            return wifeAadharNo;
        }

        public void setWifeAadharNo(String wifeAadharNo) {
            this.wifeAadharNo = wifeAadharNo;
        }

        public boolean isWifeBankAadharLinked() {
            return wifeBankAadharLinked;
        }

        public void setWifeBankAadharLinked(boolean wifeBankAadharLinked) {
            this.wifeBankAadharLinked = wifeBankAadharLinked;
        }

        public String getWifeBankName() {
            return wifeBankName;
        }

        public void setWifeBankName(String wifeBankName) {
            this.wifeBankName = wifeBankName;
        }

        public String getWifeBankBranch() {
            return wifeBankBranch;
        }

        public void setWifeBankBranch(String wifeBankBranch) {
            this.wifeBankBranch = wifeBankBranch;
        }

        public String getWifeBankACNo() {
            return wifeBankACNo;
        }

        public void setWifeBankACNo(String wifeBankACNo) {
            this.wifeBankACNo = wifeBankACNo;
        }

        public String getWifeBankIFSCCode() {
            return wifeBankIFSCCode;
        }

        public void setWifeBankIFSCCode(String wifeBankIFSCCode) {
            this.wifeBankIFSCCode = wifeBankIFSCCode;
        }

        public String getAddedBy() {
            return addedBy;
        }

        public void setAddedBy(String addedBy) {
            this.addedBy = addedBy;
        }

        public String getAddedOn() {
            return addedOn;
        }

        public void setAddedOn(String addedOn) {
            this.addedOn = addedOn;
        }
    }

    public static class RchGeneration {

        public RchGeneration() {
        }

        @SerializedName("rchId")
        @Expose
        private String rchId;
        @SerializedName("generatedDateTime")
        @Expose
        private String generatedDateTime;

        public String getRchId() {
            return rchId;
        }

        public void setRchId(String rchId) {
            this.rchId = rchId;
        }

        public String getGeneratedDateTime() {
            return generatedDateTime;
        }

        public void setGeneratedDateTime(String generatedDateTime) {
            this.generatedDateTime = generatedDateTime;
        }
    }
}
