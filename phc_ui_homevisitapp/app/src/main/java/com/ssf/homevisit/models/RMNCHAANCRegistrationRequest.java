package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RMNCHAANCRegistrationRequest {

    @SerializedName("couple")
    @Expose
    private Couple couple;
    @SerializedName("mensuralPeriod")
    @Expose
    private MensuralPeriod mensuralPeriod;
    @SerializedName("pregnantWoman")
    @Expose
    private PregnantWoman pregnantWoman;
    @SerializedName("medicalHistory")
    @Expose
    private List<String> medicalHistory;
    @SerializedName("countPregnancies")
    @Expose
    private int countPregnancies;
    @SerializedName("pregnancies")
    @Expose
    private List<Pregnancy> pregnancies;
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

    public RMNCHAANCRegistrationRequest(Couple couple,
                                        MensuralPeriod mensuralPeriod,
                                        PregnantWoman pregnantWoman,
                                        List<Pregnancy> pregnancies,
                                        List<String> medicalHistory,
                                        int countPregnancies,
                                        String expectedDeliveryFacilityType,
                                        String expectedDeliveryFacility,
                                        boolean isVDRLTestCompleted,
                                        String vdrlTestDate,
                                        String vdrlTestResult,
                                        boolean isHIVTestCompleted,
                                        String hivTestDate,
                                        String hivTestResult,
                                        boolean isHBsAGTestCompleted,
                                        String hbsagTestDate,
                                        String hbsagTestResult,
                                        String dataEntryStatus) {

        this.couple = couple;
        this.mensuralPeriod = mensuralPeriod;
        this.pregnantWoman = pregnantWoman;
        this.pregnancies = pregnancies;
        this.medicalHistory = medicalHistory;
        this.countPregnancies = countPregnancies;
        this.expectedDeliveryFacilityType = expectedDeliveryFacilityType;
        this.expectedDeliveryFacility = expectedDeliveryFacility;
        this.isVDRLTestCompleted = isVDRLTestCompleted;
        this.vdrlTestDate = vdrlTestDate;
        this.vdrlTestResult = vdrlTestResult;
        this.isHIVTestCompleted = isHIVTestCompleted;
        this.hivTestDate = hivTestDate;
        this.hivTestResult = hivTestResult;
        this.isHBsAGTestCompleted = isHBsAGTestCompleted;
        this.hbsagTestDate = hbsagTestDate;
        this.hbsagTestResult = hbsagTestResult;
        this.dataEntryStatus = dataEntryStatus;
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

        public Couple(String husbandId, String husbandName, String husbandPhone,
                      String wifeId, String wifeName, String wifePhone,
                      String address, String ecSerialNumber, String registeredBy,
                      String registeredByName, String registeredOn,
                      String rchId) {
            this.husbandId = husbandId;
            this.husbandName = husbandName;
            this.husbandPhone = husbandPhone;
            this.wifeId = wifeId;
            this.wifeName = wifeName;
            this.wifePhone = wifePhone;
            this.address = address;
            this.ecSerialNumber = ecSerialNumber;
            this.registeredBy = registeredBy;
            this.registeredByName = registeredByName;
            this.registeredOn = registeredOn;
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

        public MensuralPeriod(String lmpDate, String eddDate, boolean isRegWithin12w, boolean isReferredToPHC) {
            this.lmpDate = lmpDate;
            this.eddDate = eddDate;
            this.isRegWithin12w = isRegWithin12w;
            this.isReferredToPHC = isReferredToPHC;
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

        public PregnantWoman(int weight, int height, String bloodGroup) {
            this.weight = weight;
            this.height = height;
            this.bloodGroup = bloodGroup;
        }
    }

    public static class Pregnancy {
        @SerializedName("pregnancyNo")
        @Expose
        private int pregnancyNo;
        @SerializedName("outcome")
        @Expose
        private String outcome;
        @SerializedName("complication")
        @Expose
        private List<String> complication;

        public Pregnancy(int pregnancyNo, List<String> complication, String outcome) {
            this.pregnancyNo = pregnancyNo;
            this.complication = complication;
        }
    }

}
