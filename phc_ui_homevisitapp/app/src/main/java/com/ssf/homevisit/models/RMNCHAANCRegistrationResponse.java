package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RMNCHAANCRegistrationResponse {


    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("errors")
    @Expose
    private List<Error> errors;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("couple")
    @Expose
    private RMNCHAANCRegistrationResponse.Couple couple;
    @SerializedName("mensuralPeriod")
    @Expose
    private RMNCHAANCRegistrationResponse.MensuralPeriod mensuralPeriod;
    @SerializedName("pregnantWoman")
    @Expose
    private RMNCHAANCRegistrationResponse.PregnantWoman pregnantWoman;
    @SerializedName("medicalHistory")
    @Expose
    private List<String> medicalHistory;
    @SerializedName("countPregnancies")
    @Expose
    private int countPregnancies;
    @SerializedName("pregnancies")
    @Expose
    private List<Pregnancies> pregnancies;
    @SerializedName("expectedDeliveryFacilityType")
    @Expose
    private String expectedDeliveryFacilityType;
    @SerializedName("expectedDeliveryFacility")
    @Expose
    private String expectedDeliveryFacility;
    @SerializedName("isVDRLTestCompleted")
    @Expose
    private boolean isVDRLTestCompleted;
    @SerializedName("vdrlTestDate")
    @Expose
    private String vdrlTestDate;
    @SerializedName("vdrlTestResult")
    @Expose
    private String vdrlTestResult;
    @SerializedName("isHIVTestCompleted")
    @Expose
    private boolean isHIVTestCompleted;
    @SerializedName("hivTestDate")
    @Expose
    private String hivTestDate;
    @SerializedName("hivTestResult")
    @Expose
    private String hivTestResult;
    @SerializedName("isHBsAGTestCompleted")
    @Expose
    private boolean isHBsAGTestCompleted;
    @SerializedName("hbsagTestDate")
    @Expose
    private String hbsagTestDate;
    @SerializedName("hbsagTestResult")
    @Expose
    private String hbsagTestResult;
    @SerializedName("dataEntryStatus")
    @Expose
    private String dataEntryStatus;

    public RMNCHAANCRegistrationResponse() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Couple getCouple() {
        return couple;
    }

    public void setCouple(Couple couple) {
        this.couple = couple;
    }

    public MensuralPeriod getMensuralPeriod() {
        return mensuralPeriod;
    }

    public void setMensuralPeriod(MensuralPeriod mensuralPeriod) {
        this.mensuralPeriod = mensuralPeriod;
    }

    public PregnantWoman getPregnantWoman() {
        return pregnantWoman;
    }

    public void setPregnantWoman(PregnantWoman pregnantWoman) {
        this.pregnantWoman = pregnantWoman;
    }

    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<String> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public int getCountPregnancies() {
        return countPregnancies;
    }

    public void setCountPregnancies(int countPregnancies) {
        this.countPregnancies = countPregnancies;
    }

    public List<Pregnancies> getPregnancies() {
        return pregnancies;
    }

    public void setPregnancies(List<Pregnancies> pregnancies) {
        this.pregnancies = pregnancies;
    }

    public String getExpectedDeliveryFacilityType() {
        return expectedDeliveryFacilityType;
    }

    public void setExpectedDeliveryFacilityType(String expectedDeliveryFacilityType) {
        this.expectedDeliveryFacilityType = expectedDeliveryFacilityType;
    }

    public String getExpectedDeliveryFacility() {
        return expectedDeliveryFacility;
    }

    public void setExpectedDeliveryFacility(String expectedDeliveryFacility) {
        this.expectedDeliveryFacility = expectedDeliveryFacility;
    }

    public boolean isVDRLTestCompleted() {
        return isVDRLTestCompleted;
    }

    public void setVDRLTestCompleted(boolean VDRLTestCompleted) {
        isVDRLTestCompleted = VDRLTestCompleted;
    }

    public String getVdrlTestDate() {
        return vdrlTestDate;
    }

    public void setVdrlTestDate(String vdrlTestDate) {
        this.vdrlTestDate = vdrlTestDate;
    }

    public String getVdrlTestResult() {
        return vdrlTestResult;
    }

    public void setVdrlTestResult(String vdrlTestResult) {
        this.vdrlTestResult = vdrlTestResult;
    }

    public boolean isHIVTestCompleted() {
        return isHIVTestCompleted;
    }

    public void setHIVTestCompleted(boolean HIVTestCompleted) {
        isHIVTestCompleted = HIVTestCompleted;
    }

    public String getHivTestDate() {
        return hivTestDate;
    }

    public void setHivTestDate(String hivTestDate) {
        this.hivTestDate = hivTestDate;
    }

    public String getHivTestResult() {
        return hivTestResult;
    }

    public void setHivTestResult(String hivTestResult) {
        this.hivTestResult = hivTestResult;
    }

    public boolean isHBsAGTestCompleted() {
        return isHBsAGTestCompleted;
    }

    public void setHBsAGTestCompleted(boolean HBsAGTestCompleted) {
        isHBsAGTestCompleted = HBsAGTestCompleted;
    }

    public String getHbsagTestDate() {
        return hbsagTestDate;
    }

    public void setHbsagTestDate(String hbsagTestDate) {
        this.hbsagTestDate = hbsagTestDate;
    }

    public String getHbsagTestResult() {
        return hbsagTestResult;
    }

    public void setHbsagTestResult(String hbsagTestResult) {
        this.hbsagTestResult = hbsagTestResult;
    }

    public String getDataEntryStatus() {
        return dataEntryStatus;
    }

    public void setDataEntryStatus(String dataEntryStatus) {
        this.dataEntryStatus = dataEntryStatus;
    }

    public static class Error {
        @SerializedName("errorCode")
        @Expose
        private String errorCode;
        @SerializedName("field")
        @Expose
        private String field;
        @SerializedName("message")
        @Expose
        private String message;

        public Error() {
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Couple {
        @SerializedName("husbandId")
        @Expose
        private String husbandId;
        @SerializedName("husbandName")
        @Expose
        private String husbandName;
        @SerializedName("husbandPhone")
        @Expose
        private String husbandPhone;
        @SerializedName("wifeId")
        @Expose
        private String wifeId;
        @SerializedName("wifeName")
        @Expose
        private String wifeName;
        @SerializedName("wifePhone")
        @Expose
        private String wifePhone;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("ecSerialNumber")
        @Expose
        private String ecSerialNumber;
        @SerializedName("registeredBy")
        @Expose
        private String registeredBy;
        @SerializedName("registeredByName")
        @Expose
        private String registeredByName;
        @SerializedName("registeredOn")
        @Expose
        private String registeredOn;
        @SerializedName("rchId")
        @Expose
        private String rchId;

        public Couple() {
        }

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEcSerialNumber() {
            return ecSerialNumber;
        }

        public void setEcSerialNumber(String ecSerialNumber) {
            this.ecSerialNumber = ecSerialNumber;
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

        public String getRchId() {
            return rchId;
        }

        public void setRchId(String rchId) {
            this.rchId = rchId;
        }
    }

    public static class MensuralPeriod {
        @SerializedName("lmpDate")
        @Expose
        private String lmpDate;
        @SerializedName("eddDate")
        @Expose
        private String eddDate;
        @SerializedName("isRegWithin12w")
        @Expose
        private boolean isRegWithin12w;
        @SerializedName("isReferredToPHC")
        @Expose
        private boolean isReferredToPHC;

        public MensuralPeriod() {
        }

        public String getLmpDate() {
            return lmpDate;
        }

        public String getEddDate() {
            return eddDate;
        }

        public void setEddDate(String eddDate) {
            this.eddDate = eddDate;
        }

        public void setLmpDate(String lmpDate) {
            this.lmpDate = lmpDate;
        }
    }

    public static class PregnantWoman {
        @SerializedName("weight")
        @Expose
        private int weight;
        @SerializedName("height")
        @Expose
        private int height;
        @SerializedName("bloodGroup")
        @Expose
        private String bloodGroup;

        public PregnantWoman() {
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }
    }

    public static class Pregnancies {
        @SerializedName("pregnancyNo")
        @Expose
        private int pregnancyNo;
        @SerializedName("complication")
        @Expose
        private List<String> complication;

        public Pregnancies() {
        }

        public int getPregnancyNo() {
            return pregnancyNo;
        }

        public void setPregnancyNo(int pregnancyNo) {
            this.pregnancyNo = pregnancyNo;
        }

        public List<String> getComplication() {
            return complication;
        }

        public void setComplication(List<String> complication) {
            this.complication = complication;
        }
    }
}
