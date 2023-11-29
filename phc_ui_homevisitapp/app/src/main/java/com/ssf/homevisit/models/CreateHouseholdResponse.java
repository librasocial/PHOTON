package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateHouseholdResponse {
    @SerializedName("typeOfRoadInFrontOfHouse")
    @Expose
    private String typeOfRoadInFrontOfHouse;
    @SerializedName("motherTongue")
    @Expose
    private String motherTongue;
    @SerializedName("smokeVent")
    @Expose
    private String smokeVent;
    @SerializedName("numberOfLivingRooms")
    @Expose
    private String numberOfLivingRooms;
    @SerializedName("fuelUsedForCooking")
    @Expose
    private String fuelUsedForCooking;
    @SerializedName("typeOfHouse")
    @Expose
    private String typeOfHouse;
    @SerializedName("crossVentilation")
    @Expose
    private String crossVentilation;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("whichIsThePreferredHealthCareFacilityForAnyIllness")
    @Expose
    private String whichIsThePreferredHealthCareFacilityForAnyIllness;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("drainageSystem")
    @Expose
    private String drainageSystem;
    @SerializedName("kitchenType")
    @Expose
    private String kitchenType;
    @SerializedName("everyDayNeedsWater")
    @Expose
    private String everyDayNeedsWater;
    @SerializedName("typeOfFamily")
    @Expose
    private String typeOfFamily;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("houseID")
    @Expose
    private String houseID;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("drinkingWater")
    @Expose
    private String drinkingWater;
    @SerializedName("dateModified")
    @Expose
    private String dateModified;
    @SerializedName("physicalLocationOfHousehold")
    @Expose
    private String physicalLocationOfHousehold;
    @SerializedName("toiletFacilities")
    @Expose
    private String toiletFacilities;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("anmHealthWorkerVisitToHousehold")
    @Expose
    private String anmHealthWorkerVisitToHousehold;
    @SerializedName("headOfFamily")
    @Expose
    private String headOfFamily;
    @SerializedName("availabilityOfElectricity")
    @Expose
    private String availabilityOfElectricity;
    @SerializedName("houseOwnership")
    @Expose
    private String houseOwnership;
    @SerializedName("selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation")
    @Expose
    private String selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("bathingArea")
    @Expose
    private String bathingArea;
    @SerializedName("casteCategory")
    @Expose
    private String casteCategory;
    @SerializedName("village")
    @Expose
    private VillageProperties villlage;
    public String getTypeOfRoadInFrontOfHouse() {
        return typeOfRoadInFrontOfHouse;
    }

    public void setTypeOfRoadInFrontOfHouse(String typeOfRoadInFrontOfHouse) {
        this.typeOfRoadInFrontOfHouse = typeOfRoadInFrontOfHouse;
    }

    public String getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }

    public String getSmokeVent() {
        return smokeVent;
    }

    public void setSmokeVent(String smokeVent) {
        this.smokeVent = smokeVent;
    }

    public String getNumberOfLivingRooms() {
        return numberOfLivingRooms;
    }

    public void setNumberOfLivingRooms(String numberOfLivingRooms) {
        this.numberOfLivingRooms = numberOfLivingRooms;
    }

    public String getFuelUsedForCooking() {
        return fuelUsedForCooking;
    }

    public void setFuelUsedForCooking(String fuelUsedForCooking) {
        this.fuelUsedForCooking = fuelUsedForCooking;
    }

    public String getTypeOfHouse() {
        return typeOfHouse;
    }

    public void setTypeOfHouse(String typeOfHouse) {
        this.typeOfHouse = typeOfHouse;
    }

    public String getCrossVentilation() {
        return crossVentilation;
    }

    public void setCrossVentilation(String crossVentilation) {
        this.crossVentilation = crossVentilation;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWhichIsThePreferredHealthCareFacilityForAnyIllness() {
        return whichIsThePreferredHealthCareFacilityForAnyIllness;
    }

    public void setWhichIsThePreferredHealthCareFacilityForAnyIllness(String whichIsThePreferredHealthCareFacilityForAnyIllness) {
        this.whichIsThePreferredHealthCareFacilityForAnyIllness = whichIsThePreferredHealthCareFacilityForAnyIllness;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDrainageSystem() {
        return drainageSystem;
    }

    public void setDrainageSystem(String drainageSystem) {
        this.drainageSystem = drainageSystem;
    }

    public String getKitchenType() {
        return kitchenType;
    }

    public void setKitchenType(String kitchenType) {
        this.kitchenType = kitchenType;
    }

    public String getEveryDayNeedsWater() {
        return everyDayNeedsWater;
    }

    public void setEveryDayNeedsWater(String everyDayNeedsWater) {
        this.everyDayNeedsWater = everyDayNeedsWater;
    }

    public String getTypeOfFamily() {
        return typeOfFamily;
    }

    public void setTypeOfFamily(String typeOfFamily) {
        this.typeOfFamily = typeOfFamily;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDrinkingWater() {
        return drinkingWater;
    }

    public void setDrinkingWater(String drinkingWater) {
        this.drinkingWater = drinkingWater;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getPhysicalLocationOfHousehold() {
        return physicalLocationOfHousehold;
    }

    public void setPhysicalLocationOfHousehold(String physicalLocationOfHousehold) {
        this.physicalLocationOfHousehold = physicalLocationOfHousehold;
    }

    public String getToiletFacilities() {
        return toiletFacilities;
    }

    public void setToiletFacilities(String toiletFacilities) {
        this.toiletFacilities = toiletFacilities;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getAnmHealthWorkerVisitToHousehold() {
        return anmHealthWorkerVisitToHousehold;
    }

    public void setAnmHealthWorkerVisitToHousehold(String anmHealthWorkerVisitToHousehold) {
        this.anmHealthWorkerVisitToHousehold = anmHealthWorkerVisitToHousehold;
    }

    public String getHeadOfFamily() {
        return headOfFamily;
    }

    public void setHeadOfFamily(String headOfFamily) {
        this.headOfFamily = headOfFamily;
    }

    public String getAvailabilityOfElectricity() {
        return availabilityOfElectricity;
    }

    public void setAvailabilityOfElectricity(String availabilityOfElectricity) {
        this.availabilityOfElectricity = availabilityOfElectricity;
    }

    public String getHouseOwnership() {
        return houseOwnership;
    }

    public void setHouseOwnership(String houseOwnership) {
        this.houseOwnership = houseOwnership;
    }

    public String getSelectTheAppropriateOptionForTheHouseholdBasedOnYourObservation() {
        return selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation;
    }

    public void setSelectTheAppropriateOptionForTheHouseholdBasedOnYourObservation(String selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation) {
        this.selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation = selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getBathingArea() {
        return bathingArea;
    }

    public void setBathingArea(String bathingArea) {
        this.bathingArea = bathingArea;
    }

    public String getCasteCategory() {
        return casteCategory;
    }

    public void setCasteCategory(String casteCategory) {
        this.casteCategory = casteCategory;
    }

    public VillageProperties getVilllage() {
        return villlage;
    }

    public void setVilllage(VillageProperties villlage) {
        this.villlage = villlage;
    }

    public static HouseHoldProperties houseHoldResponseToHouseHoldProperties(CreateHouseholdResponse createHouseholdResponse) {
        HouseHoldProperties houseHoldProperties = new HouseHoldProperties();
        if(createHouseholdResponse.getUuid() != null){
            houseHoldProperties.setUuid(createHouseholdResponse.getUuid());
        }
        if(createHouseholdResponse.getVilllage()!=null){
            houseHoldProperties.setVillage(createHouseholdResponse.getVilllage().getUuid());
        }
        houseHoldProperties.setLatitude(createHouseholdResponse.getLatitude());
        houseHoldProperties.setLongitude(createHouseholdResponse.getLongitude());
        if(createHouseholdResponse.getHouseID() != null){
            houseHoldProperties.setHouseID(createHouseholdResponse.getHouseID());
        }
        return houseHoldProperties;
    }
}
