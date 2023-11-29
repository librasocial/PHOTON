package com.ssf.homevisit.models;

import java.util.List;

public class HouseHoldProperties {
    private String uuid;
    private String houseID;
    private String latitude;
    private String longitude;
    private String houseHeadName;
    private int totalFamilyMembers;
    private String dateCreated;
    private String createdBy;
    private String dateModified;
    private String modifiedBy;
    private String village;
    private String[] houseHeadImageUrls;

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

    public double getLatitude() {
        try {
            return Double.parseDouble(latitude);
        } catch (Exception e) {
            return 0d;
        }
    }

    public void setLatitude(String lattitude) {
        this.latitude = lattitude;
    }

    public void setLatitude(Double lattitude) {
        if(lattitude == null) lattitude = 0d;
        this.latitude = lattitude + "";
    }

    public double getLongitude() {
        try {
            return Double.parseDouble(longitude);
        } catch (Exception e) {
            return 0d;
        }
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLongitude(Double longitude) {
        if(longitude == null) longitude = 0d;
        this.longitude = longitude + "";
    }

    public String getHouseHeadName() {
        return houseHeadName;
    }

    public void setHouseHeadName(String houseHeadName) {
        this.houseHeadName = houseHeadName;
    }

    public Integer getTotalFamilyMembers() {
        return totalFamilyMembers;
    }

    public void setTotalFamilyMembers(Integer totalFamilyMembers) {
        this.totalFamilyMembers = totalFamilyMembers;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateCreated(List<Integer> dateCreated) {
        int year = dateCreated.size() > 0 ? dateCreated.get(0) : 2000;
        int month = dateCreated.size() > 1 ? dateCreated.get(1) : 1;
        int dayOfMonth = dateCreated.size() > 2 ? dateCreated.get(2) : 1;
        int hour = dateCreated.size() > 3 ? dateCreated.get(3) : 1;
        int min = dateCreated.size() > 4 ? dateCreated.get(4) : 1;
        String date = year + "-" + month + "-" + dayOfMonth + "T" + hour + ":" + min;
        this.dateCreated = date;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setTotalFamilyMembers(int totalFamilyMembers) {
        this.totalFamilyMembers = totalFamilyMembers;
    }

    public String[] getHouseHeadImageUrls() {
        return houseHeadImageUrls;
    }

    public void setHouseHeadImageUrls(String[] houseHeadImageUrls) {
        this.houseHeadImageUrls = houseHeadImageUrls;
    }

}