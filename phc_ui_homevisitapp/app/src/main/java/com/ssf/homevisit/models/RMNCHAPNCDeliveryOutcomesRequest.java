package com.ssf.homevisit.models;

public class RMNCHAPNCDeliveryOutcomesRequest {

    private Couple couple;
    private MensuralPeriod mensuralPeriod;
    private DeliveryDetails deliveryDetails;
    private boolean isCovidTestDone;
    private boolean isCovidResultPositive;
    private boolean isILIExperienced;
    private boolean didContactCovidPatient;

    public Couple getCouple() {
        return couple;
    }

    public void setCouple(Couple couple) {
        this.couple = couple;
    }

    public MensuralPeriod getMensuralPeriod() {
        return mensuralPeriod;
    }

    public void setMenstrualPeriod(MensuralPeriod menstrualPeriod) {
        this.mensuralPeriod = menstrualPeriod;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
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

    public static class Couple{
        private String husbandId;
        private String husbandName;
        private String wifeId;
        private String wifeName;
        private int ecSerialNumber;
        private String registeredBy;
        private String registeredByName;
        private String registeredOn;
        private String rchId;
        private String lastANCVisitDate;

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

        public int getEcSerialNumber() {
            return ecSerialNumber;
        }

        public void setEcSerialNumber(int ecSerialNumber) {
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

        public String getLastANCVisitDate() {
            return lastANCVisitDate;
        }

        public void setLastANCVisitDate(String lastANCVisitDate) {
            this.lastANCVisitDate = lastANCVisitDate;
        }
    }

    public static class DeliveryDetails{
        private String deliveryDate;
        private String financialYear;
        private String place;
        private String location;
        private String conductedBy;
        private String deliveryType;
        private int deliveryOutcome;
        private int liveBirthCount;
        private int stillBirthCount;
        private String complications;
        private String dischargeDateTime;

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public String getFinancialYear() {
            return financialYear;
        }

        public void setFinancialYear(String financialYear) {
            this.financialYear = financialYear;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getConductedBy() {
            return conductedBy;
        }

        public void setConductedBy(String conductedBy) {
            this.conductedBy = conductedBy;
        }

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public int getDeliveryOutcome() {
            return deliveryOutcome;
        }

        public void setDeliveryOutcome(int deliveryOutcome) {
            this.deliveryOutcome = deliveryOutcome;
        }

        public int getLiveBirthCount() {
            return liveBirthCount;
        }

        public void setLiveBirthCount(int liveBirthCount) {
            this.liveBirthCount = liveBirthCount;
        }

        public int getStillBirthCount() {
            return stillBirthCount;
        }

        public void setStillBirthCount(int stillBirthCount) {
            this.stillBirthCount = stillBirthCount;
        }

        public String getComplications() {
            return complications;
        }

        public void setComplications(String complications) {
            this.complications = complications;
        }

        public String getDischargeDateTime() {
            return dischargeDateTime;
        }

        public void setDischargeDateTime(String dischargeDateTime) {
            this.dischargeDateTime = dischargeDateTime;
        }
    }

    public static class MensuralPeriod{
        private String lmpDate;
        private String eddDate;

        public String getLmpDate() {
            return lmpDate;
        }

        public void setLmpDate(String lmpDate) {
            this.lmpDate = lmpDate;
        }

        public String getEddDate() {
            return eddDate;
        }

        public void setEddDate(String eddDate) {
            this.eddDate = eddDate;
        }
    }

}
