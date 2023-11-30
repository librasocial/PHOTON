package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

import java.util.Date;

@SuppressWarnings("ALL")
public class CreateResidentResponse {
    @SuppressWarnings("unused")
        // TODO refract it completely according to incoming data
        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("contact")
        @Expose
        private String contact;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("caste")
        @Expose
        private String caste;
        @SerializedName("age")
        @Expose
        private long age;
        @SerializedName("healthID")
        @Expose
        private String health_id;
        @SerializedName("firstName")
        @Expose
        private String first_name;
        @SerializedName("middleName")
        @Expose
        private String middle_name;
        @SerializedName("lastName")
        @Expose
        private String last_name;
        @SerializedName("dateOfBirth")
        @Expose
        private Object date_of_birth;
        @SerializedName("isHead")
        @Expose
        private Boolean is_head;
        @SerializedName("motherTongue")
        @Expose
        private String motherTongue;
        @SerializedName("annualIncome")
        @Expose
        private String annualIncome;
        @SerializedName("workingInVillage")
        @Expose
        private String workingInVillage;
        @SerializedName("residingInVillage")
        @Expose
        private String residingInVillage;
        @SerializedName("highestQualification")
        @Expose
        private String highestQualification;
        @SerializedName("idProof")
        @Expose
        private String idProof;
        @SerializedName("bloodGroup")
        @Expose
        private String bloodGroup;
        @SerializedName("previousOccupation")
        @Expose
        private String previousOccupation;
        @SerializedName("disabled")
        @Expose
        private boolean disabled;
        @SerializedName("receivesSSF")
        @Expose
        private String receivesSSF;
        @SerializedName("presentOccupation")
        @Expose
        private String presentOccupation;
        @SerializedName("relationship")
        @Expose
        private String relationship;
        @SerializedName("povertyStatus")
        @Expose
        private String povertyStatus;
        @SerializedName("writingLanguages")
        @Expose
        private String writingLanguages;
        @SerializedName("readingLanguages")
        @Expose
        private String readingLanguages;
        @SerializedName("typeOfDisabilities")
        @Expose
        private String typeOfDisabilities;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("spokenLanguages")
        @Expose
        private String spokenLanguages;
        @SerializedName("sourceOfIncome")
        @Expose
        private String sourceOfIncome;
        @SerializedName("maritalStatus")
        @Expose
        private String maritalStatus;
        @SerializedName("houseHold")
        @Expose
        private JSONObject houseHold;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCaste() {
            return caste;
        }

        public void setCaste(String caste) {
            this.caste = caste;
        }

        public long getAge() {
            return age;
        }

        public void setAge(long age) {
            this.age = age;
        }

        public String getHealth_id() {
            return health_id;
        }

        public void setHealth_id(String health_id) {
            this.health_id = health_id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getMiddle_name() {
            return middle_name;
        }

        public void setMiddle_name(String middle_name) {
            this.middle_name = middle_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public Date getDate_of_birth() {
            try {
                return (Date) date_of_birth;
            }catch (Exception e){
                return null;
            }
        }

        public void setDate_of_birth(Date date_of_birth) {
            this.date_of_birth = date_of_birth;
        }

        public boolean getIs_head() {
            if(this.is_head == null ) return false;
            return is_head;
        }

        public void setIs_head(boolean is_head) {
            this.is_head = is_head;
        }

        public void setHouseHold(JSONObject houseID) {
            this.houseHold = houseID;
        }

        public String getMotherTongue() {
            return motherTongue;
        }

        public void setMotherTongue(String motherTongue) {
            this.motherTongue = motherTongue;
        }

        public String getAnnualIncome() {
            return annualIncome;
        }

        public void setAnnualIncome(String annualIncome) {
            this.annualIncome = annualIncome;
        }

        public String getWorkingInVillage() {
            return workingInVillage;
        }

        public void setWorkingInVillage(String workingInVillage) {
            this.workingInVillage = workingInVillage;
        }

        public String getResidingInVillage() {
            return residingInVillage;
        }

        public void setResidingInVillage(String residingInVillage) {
            this.residingInVillage = residingInVillage;
        }

        public String getHighestQualification() {
            return highestQualification;
        }

        public void setHighestQualification(String highestQualification) {
            this.highestQualification = highestQualification;
        }

        public String getIdProof() {
            return idProof;
        }

        public void setIdProof(String idProof) {
            this.idProof = idProof;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }

        public String getPreviousOccupation() {
            return previousOccupation;
        }

        public void setPreviousOccupation(String previousOccupation) {
            this.previousOccupation = previousOccupation;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public String getReceivesSSF() {
            return receivesSSF;
        }

        public void setReceivesSSF(String receivesSSF) {
            this.receivesSSF = receivesSSF;
        }

        public String getPresentOccupation() {
            return presentOccupation;
        }

        public void setPresentOccupation(String presentOccupation) {
            this.presentOccupation = presentOccupation;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getPovertyStatus() {
            return povertyStatus;
        }

        public void setPovertyStatus(String povertyStatus) {
            this.povertyStatus = povertyStatus;
        }

        public String getWritingLanguages() {
            return writingLanguages;
        }

        public void setWritingLanguages(String writingLanguages) {
            this.writingLanguages = writingLanguages;
        }

        public String getReadingLanguages() {
            return readingLanguages;
        }

        public void setReadingLanguages(String readingLanguages) {
            this.readingLanguages = readingLanguages;
        }

        public String getTypeOfDisabilities() {
            return typeOfDisabilities;
        }

        public void setTypeOfDisabilities(String typeOfDisabilities) {
            this.typeOfDisabilities = typeOfDisabilities;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getSpokenLanguages() {
            return spokenLanguages;
        }

        public void setSpokenLanguages(String spokenLanguages) {
            this.spokenLanguages = spokenLanguages;
        }

        public String getSourceOfIncome() {
            return sourceOfIncome;
        }

        public void setSourceOfIncome(String sourceOfIncome) {
            this.sourceOfIncome = sourceOfIncome;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public JSONObject getHouseHold() {
            return houseHold;
        }



}
