package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class SearchHouseholdProperties {

    private String uuid;
    private String houseID;
    private Double latitude;
    private Double longitude;
    private Integer totalFamilyMembers;
    private String typeOfFamily;
    private String houseOwnership;
    private String typeOfRoadInFrontOfHouse;
    private String physicalLocationOfHousehold;
    private String typeOfHouse;
    private Integer numberOfLivingRooms;
    private Boolean crossVentilation;
    private Boolean availabilityOfElectricity;
    private String drainageSystem;
    private String drinkingWater;
    private String sourceOfWater;
    private String toiletFacilities;
    private String bathingArea;
    private String preferredHealthCare;
    private String anmVisit;
    private String appOptOfHouseBasedOnObservation;
    private LocalDateTime dateCreated;
    private String createdBy;
    private LocalDateTime dateModified;
    private String modifiedBy;
    private Boolean smokeVent;
    private String fuelUsedForCooking;
    private String kitchenType;

    public SearchHouseholdProperties() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalFamilyMembers() {
        return totalFamilyMembers;
    }

    public void setTotalFamilyMembers(Integer totalFamilyMembers) {
        this.totalFamilyMembers = totalFamilyMembers;
    }

    public String getTypeOfFamily() {
        return typeOfFamily;
    }

    public void setTypeOfFamily(String typeOfFamily) {
        this.typeOfFamily = typeOfFamily;
    }

    public String getHouseOwnership() {
        return houseOwnership;
    }

    public void setHouseOwnership(String houseOwnership) {
        this.houseOwnership = houseOwnership;
    }

    public String getTypeOfRoadInFrontOfHouse() {
        return typeOfRoadInFrontOfHouse;
    }

    public void setTypeOfRoadInFrontOfHouse(String typeOfRoadInFrontOfHouse) {
        this.typeOfRoadInFrontOfHouse = typeOfRoadInFrontOfHouse;
    }

    public String getPhysicalLocationOfHousehold() {
        return physicalLocationOfHousehold;
    }

    public void setPhysicalLocationOfHousehold(String physicalLocationOfHousehold) {
        this.physicalLocationOfHousehold = physicalLocationOfHousehold;
    }

    public String getTypeOfHouse() {
        return typeOfHouse;
    }

    public void setTypeOfHouse(String typeOfHouse) {
        this.typeOfHouse = typeOfHouse;
    }

    public Integer getNumberOfLivingRooms() {
        return numberOfLivingRooms;
    }

    public void setNumberOfLivingRooms(Integer numberOfLivingRooms) {
        this.numberOfLivingRooms = numberOfLivingRooms;
    }

    public Boolean getCrossVentilation() {
        return crossVentilation;
    }

    public void setCrossVentilation(Boolean crossVentilation) {
        this.crossVentilation = crossVentilation;
    }

    public Boolean getAvailabilityOfElectricity() {
        return availabilityOfElectricity;
    }

    public void setAvailabilityOfElectricity(Boolean availabilityOfElectricity) {
        this.availabilityOfElectricity = availabilityOfElectricity;
    }

    public String getDrainageSystem() {
        return drainageSystem;
    }

    public void setDrainageSystem(String drainageSystem) {
        this.drainageSystem = drainageSystem;
    }

    public String getDrinkingWater() {
        return drinkingWater;
    }

    public void setDrinkingWater(String drinkingWater) {
        this.drinkingWater = drinkingWater;
    }

    public String getSourceOfWater() {
        return sourceOfWater;
    }

    public void setSourceOfWater(String sourceOfWater) {
        this.sourceOfWater = sourceOfWater;
    }

    public String getToiletFacilities() {
        return toiletFacilities;
    }

    public void setToiletFacilities(String toiletFacilities) {
        this.toiletFacilities = toiletFacilities;
    }

    public String getBathingArea() {
        return bathingArea;
    }

    public void setBathingArea(String bathingArea) {
        this.bathingArea = bathingArea;
    }

    public String getPreferredHealthCare() {
        return preferredHealthCare;
    }

    public void setPreferredHealthCare(String preferredHealthCare) {
        this.preferredHealthCare = preferredHealthCare;
    }

    public String getAnmVisit() {
        return anmVisit;
    }

    public void setAnmVisit(String anmVisit) {
        this.anmVisit = anmVisit;
    }

    public String getAppOptOfHouseBasedOnObservation() {
        return appOptOfHouseBasedOnObservation;
    }

    public void setAppOptOfHouseBasedOnObservation(String appOptOfHouseBasedOnObservation) {
        this.appOptOfHouseBasedOnObservation = appOptOfHouseBasedOnObservation;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Boolean getSmokeVent() {
        return smokeVent;
    }

    public void setSmokeVent(Boolean smokeVent) {
        this.smokeVent = smokeVent;
    }

    public String getFuelUsedForCooking() {
        return fuelUsedForCooking;
    }

    public void setFuelUsedForCooking(String fuelUsedForCooking) {
        this.fuelUsedForCooking = fuelUsedForCooking;
    }

    public String getKitchenType() {
        return kitchenType;
    }

    public void setKitchenType(String kitchenType) {
        this.kitchenType = kitchenType;
    }

    public SearchHouseholdProperties(String uuid, String houseID, Double latitude, Double longitude, Integer totalFamilyMembers, String typeOfFamily, String houseOwnership, String typeOfRoadInFrontOfHouse, String physicalLocationOfHousehold, String typeOfHouse, Integer numberOfLivingRooms, Boolean crossVentilation, Boolean availabilityOfElectricity, String drainageSystem, String drinkingWater, String sourceOfWater, String toiletFacilities, String bathingArea, String preferredHealthCare, String anmVisit, String appOptOfHouseBasedOnObservation, LocalDateTime dateCreated, String createdBy, LocalDateTime dateModified, String modifiedBy, Boolean smokeVent, String fuelUsedForCooking, String kitchenType) {
        this.uuid = uuid;
        this.houseID = houseID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalFamilyMembers = totalFamilyMembers;
        this.typeOfFamily = typeOfFamily;
        this.houseOwnership = houseOwnership;
        this.typeOfRoadInFrontOfHouse = typeOfRoadInFrontOfHouse;
        this.physicalLocationOfHousehold = physicalLocationOfHousehold;
        this.typeOfHouse = typeOfHouse;
        this.numberOfLivingRooms = numberOfLivingRooms;
        this.crossVentilation = crossVentilation;
        this.availabilityOfElectricity = availabilityOfElectricity;
        this.drainageSystem = drainageSystem;
        this.drinkingWater = drinkingWater;
        this.sourceOfWater = sourceOfWater;
        this.toiletFacilities = toiletFacilities;
        this.bathingArea = bathingArea;
        this.preferredHealthCare = preferredHealthCare;
        this.anmVisit = anmVisit;
        this.appOptOfHouseBasedOnObservation = appOptOfHouseBasedOnObservation;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.dateModified = dateModified;
        this.modifiedBy = modifiedBy;
        this.smokeVent = smokeVent;
        this.fuelUsedForCooking = fuelUsedForCooking;
        this.kitchenType = kitchenType;
    }
}
