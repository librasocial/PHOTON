package com.ssf.homevisit.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CitizenProperties {

    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("residingInVillage")
    @Expose
    private String residingInVillage;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("isHead")
    @Expose
    private boolean isHead;
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
    private long age;
    @SerializedName("houseHold")
    @Expose
    private String houseHold;

    @SerializedName("relationshipWithHead")
    @Expose
    private String relationshipWithHead;
    /**
     * No args constructor for use in serialization
     */
    public CitizenProperties() {
    }

    /**
     * @param lastName
     * @param residingInVillage
     * @param gender
     * @param contact
     * @param houseHold
     * @param dateOfBirth
     * @param firstName
     * @param middleName
     * @param age
     */
    public CitizenProperties(String lastName, String residingInVillage , String gender, String dateOfBirth, boolean isHead, String contact, String firstName, String middleName,  long age, String houseHold) {
        super();
        this.lastName = lastName;
        this.residingInVillage = residingInVillage;
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

    public String getResidingInVillage() {
        return residingInVillage;
    }

    public void setResidingInVillage(String residingInVillage) {
        this.residingInVillage = residingInVillage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isIsHead() {
        return isHead;
    }

    public void setIsHead(boolean isHead) {
        this.isHead = isHead;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRelationshipWithHead() {
        return relationshipWithHead;
    }

    public void setRelationshipWithHead(String relationshipWithHead) {
        this.relationshipWithHead = relationshipWithHead;
    }
}