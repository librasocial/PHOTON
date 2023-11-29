package com.ssf.homevisit.models;

import java.util.List;

public class RMNCHAPNCInfantRequest {

    private List<Infant> infantregistration;

    public List<Infant> getInfants() {
        return infantregistration;
    }

    public void setInfants(List<Infant> infants) {
        this.infantregistration = infants;
    }

    public static class Infant {
        private int infantLiteral;
        private String childId;
        private String name;
        private String pncRegistrationId;
        private String registrationDate;
        private String dateOfBirth;
        private String financialYear;
        private String infantTerm;
        private String gender;
        private double birthWeight;
        private String defectAtBirth;
        private String didBabyCryAtBirth;
        private boolean didBreastFeedingStartWithin1Hr;
        private String wasResuscitationDone;
        private String isReferredToHigherFacility;
        private Immunization immunization;

        public String getWasResuscitationDone() {
            return wasResuscitationDone;
        }

        public void setWasResuscitationDone(String wasResuscitationDone) {
            this.wasResuscitationDone = wasResuscitationDone;
        }

        public String getChildId() {
            return childId;
        }

        public void setChildId(String childId) {
            this.childId = childId;
        }

        public String getDidBabyCryAtBirth() {
            return didBabyCryAtBirth;
        }

        public String getIsReferredToHigherFacility() {
            return isReferredToHigherFacility;
        }

        public void setIsReferredToHigherFacility(String isReferredToHigherFacility) {
            this.isReferredToHigherFacility = isReferredToHigherFacility;
        }

        public int getInfantLiteral() {
            return infantLiteral;
        }

        public void setInfantLiteral(int infantLiteral) {
            this.infantLiteral = infantLiteral;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPncRegistrationId() {
            return pncRegistrationId;
        }

        public void setPncRegistrationId(String pncRegistrationId) {
            this.pncRegistrationId = pncRegistrationId;
        }

        public String getRegistrationDate() {
            return registrationDate;
        }

        public void setRegistrationDate(String registrationDate) {
            this.registrationDate = registrationDate;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getFinancialYear() {
            return financialYear;
        }

        public void setFinancialYear(String financialYear) {
            this.financialYear = financialYear;
        }

        public String getInfantTerm() {
            return infantTerm;
        }

        public void setInfantTerm(String infantTerm) {
            this.infantTerm = infantTerm;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public double getBirthWeight() {
            return birthWeight;
        }

        public void setBirthWeight(double birthWeight) {
            this.birthWeight = birthWeight;
        }

        public String getDefectAtBirth() {
            return defectAtBirth;
        }

        public void setDefectAtBirth(String defectAtBirth) {
            this.defectAtBirth = defectAtBirth;
        }

        public String isDidBabyCryAtBirth() {
            return didBabyCryAtBirth;
        }

        public void setDidBabyCryAtBirth(String didBabyCryAtBirth) {
            this.didBabyCryAtBirth = didBabyCryAtBirth;
        }

        public boolean isDidBreastFeedingStartWithin1Hr() {
            return didBreastFeedingStartWithin1Hr;
        }

        public void setDidBreastFeedingStartWithin1Hr(boolean didBreastFeedingStartWithin1Hr) {
            this.didBreastFeedingStartWithin1Hr = didBreastFeedingStartWithin1Hr;
        }

        public String isReferredToHigherFacility() {
            return isReferredToHigherFacility;
        }

        public void setReferredToHigherFacility(String referredToHigherFacility) {
            isReferredToHigherFacility = referredToHigherFacility;
        }

        public Immunization getImmunization() {
            return immunization;
        }

        public void setImmunization(Immunization immunization) {
            this.immunization = immunization;
        }

        public static class Immunization {
            private String opv0DoseDate;
            private String bcgDoseDate;
            private String hepB0DoseDate;
            private String vitKDoseDate;

            public String getOpv0DoseDate() {
                return opv0DoseDate;
            }

            public void setOpv0DoseDate(String opv0DoseDate) {
                this.opv0DoseDate = opv0DoseDate;
            }

            public String getBcgDoseDate() {
                return bcgDoseDate;
            }

            public void setBcgDoseDate(String bcgDoseDate) {
                this.bcgDoseDate = bcgDoseDate;
            }

            public String getHepB0DoseDate() {
                return hepB0DoseDate;
            }

            public void setHepB0DoseDate(String hepB0DoseDate) {
                this.hepB0DoseDate = hepB0DoseDate;
            }

            public String getVitKDoseDate() {
                return vitKDoseDate;
            }

            public void setVitKDoseDate(String vitKDoseDate) {
                this.vitKDoseDate = vitKDoseDate;
            }
        }

    }
}
