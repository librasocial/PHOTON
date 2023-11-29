package com.ssf.homevisit.models;

import android.graphics.Bitmap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResidentProperties {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("imageUrls")
    @Expose
    private List<String> imageUrls = null;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("isHead")
    @Expose
    private Boolean isHead;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("healthID")
    @Expose
    private String healthID;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("houseHold")
    @Expose
    private String houseHold;
    @SerializedName("residingInVillage")
    @Expose
    private String residingInVillage;
    transient public Bitmap profileImage;

    @SerializedName("relationshipWithHead")
    @Expose
    private String relationshipWithHead;

    private Boolean isCommitteeMember;

    public ResidentProperties() {
    }

    public ResidentProperties(String lastName, String motherTongue, String annualIncome, List<String> imageUrls, String workingInVillage, String residingInVillage, String highestQualification, String gender, String dateOfBirth, String idProof, Boolean isHead, String bloodGroup, String previousOccupation, String contact, Boolean disabled, String receivesSSF, String presentOccupation, String relationship, String firstName, String povertyStatus, String healthID, String writingLanguages, String caste, String readingLanguages, String typeOfDisabilities, String specialization, String spokenLanguages, String middleName, String sourceOfIncome, String maritalStatus, Integer age, String houseHold) {
        this.lastName = lastName;
        this.imageUrls = imageUrls;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.isHead = isHead;
        this.contact = contact;
        this.firstName = firstName;
        this.healthID = healthID;
        this.middleName = middleName;
        this.age = age;
        this.houseHold = houseHold;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getResidingInVillage() {
        return residingInVillage;
    }

    public void setResidingInVillage(String residingInVillage) {
        this.residingInVillage = residingInVillage;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getIsHead() {
        if (isHead == null) return false;
        return isHead;
    }

    public void setIsHead(Boolean isHead) {
        this.isHead = isHead;
    }

    public String getContact() {
        return contact;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHealthID() {
        return healthID;
    }

    public void setHealthID(String healthID) {
        this.healthID = healthID;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHouseHold() {
        return houseHold;
    }

    public void setHouseHold(String houseHold) {
        this.houseHold = houseHold;
    }

//    public Bitmap getProfileImage() {
//        return profileImage;
//    }
//
//    void setProfileImage(Bitmap profileImage) {
//        this.profileImage = profileImage;
//    }

    public String getRelationshipWithHead() {
        return relationshipWithHead;
    }

    public void setRelationshipWithHead(String relationshipWithHead) {
        this.relationshipWithHead = relationshipWithHead;
    }

}