package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlacesProperties {

    @SerializedName("h_id")
    @Expose
    private String hId;
    @SerializedName("smokeVent")
    @Expose
    private String smokeVent;
    @SerializedName("numberOfLivingRooms")
    @Expose
    private String numberOfLivingRooms;
    @SerializedName("fuelUsedForCooking")
    @Expose
    private String fuelUsedForCooking;
    @SerializedName("physicalLocationOfHousehold")
    @Expose
    private String physicalLocationOfHousehold;
    @SerializedName("crossVentilation")
    @Expose
    private String crossVentilation;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("anmHealthWorkerVisitToHousehold")
    @Expose
    private String anmHealthWorkerVisitToHousehold;
    @SerializedName("whichIsThePreferredHealthCareFacilityForAnyIllness")
    @Expose
    private String whichIsThePreferredHealthCareFacilityForAnyIllness;
    @SerializedName("availabilityOfElectricity")
    @Expose
    private String availabilityOfElectricity;
    @SerializedName("selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation")
    @Expose
    private String selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation;
    @SerializedName("kitchenType")
    @Expose
    private String kitchenType;

    /**
     * No args constructor for use in serialization
     *
     */
    public PlacesProperties() {
    }

    /**
     *
     * @param hId
     * @param availabilityOfElectricity
     * @param smokeVent
     * @param numberOfLivingRooms
     * @param selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation
     * @param fuelUsedForCooking
     * @param physicalLocationOfHousehold
     * @param kitchenType
     * @param crossVentilation
     * @param uuid
     * @param anmHealthWorkerVisitToHousehold
     * @param whichIsThePreferredHealthCareFacilityForAnyIllness
     */
    public PlacesProperties(String hId, String smokeVent, String numberOfLivingRooms, String fuelUsedForCooking, String physicalLocationOfHousehold, String crossVentilation, String uuid, String anmHealthWorkerVisitToHousehold, String whichIsThePreferredHealthCareFacilityForAnyIllness, String availabilityOfElectricity, String selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation, String kitchenType) {
        super();
        this.hId = hId;
        this.smokeVent = smokeVent;
        this.numberOfLivingRooms = numberOfLivingRooms;
        this.fuelUsedForCooking = fuelUsedForCooking;
        this.physicalLocationOfHousehold = physicalLocationOfHousehold;
        this.crossVentilation = crossVentilation;
        this.uuid = uuid;
        this.anmHealthWorkerVisitToHousehold = anmHealthWorkerVisitToHousehold;
        this.whichIsThePreferredHealthCareFacilityForAnyIllness = whichIsThePreferredHealthCareFacilityForAnyIllness;
        this.availabilityOfElectricity = availabilityOfElectricity;
        this.selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation = selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation;
        this.kitchenType = kitchenType;
    }

    public String gethId() {
        return hId;
    }

    public void sethId(String hId) {
        this.hId = hId;
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

    public String getPhysicalLocationOfHousehold() {
        return physicalLocationOfHousehold;
    }

    public void setPhysicalLocationOfHousehold(String physicalLocationOfHousehold) {
        this.physicalLocationOfHousehold = physicalLocationOfHousehold;
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

    public String getAnmHealthWorkerVisitToHousehold() {
        return anmHealthWorkerVisitToHousehold;
    }

    public void setAnmHealthWorkerVisitToHousehold(String anmHealthWorkerVisitToHousehold) {
        this.anmHealthWorkerVisitToHousehold = anmHealthWorkerVisitToHousehold;
    }

    public String getWhichIsThePreferredHealthCareFacilityForAnyIllness() {
        return whichIsThePreferredHealthCareFacilityForAnyIllness;
    }

    public void setWhichIsThePreferredHealthCareFacilityForAnyIllness(String whichIsThePreferredHealthCareFacilityForAnyIllness) {
        this.whichIsThePreferredHealthCareFacilityForAnyIllness = whichIsThePreferredHealthCareFacilityForAnyIllness;
    }

    public String getAvailabilityOfElectricity() {
        return availabilityOfElectricity;
    }

    public void setAvailabilityOfElectricity(String availabilityOfElectricity) {
        this.availabilityOfElectricity = availabilityOfElectricity;
    }

    public String getSelectTheAppropriateOptionForTheHouseholdBasedOnYourObservation() {
        return selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation;
    }

    public void setSelectTheAppropriateOptionForTheHouseholdBasedOnYourObservation(String selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation) {
        this.selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation = selectTheAppropriateOptionForTheHouseholdBasedOnYourObservation;
    }

    public String getKitchenType() {
        return kitchenType;
    }

    public void setKitchenType(String kitchenType) {
        this.kitchenType = kitchenType;
    }

    
}