package com.ssf.homevisit.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurveyCubeHouseholdBody {

    @SerializedName("hID")
    @Expose
    private String hID;
    @SerializedName("dateFamily")
    @Expose
    private DateFamily dateFamily;
    @SerializedName("populationInHouseHold")
    @Expose
    private PopulationInHouseHold populationInHouseHold;
    @SerializedName("personsWithDisabilities")
    @Expose
    private PersonsWithDisabilities personsWithDisabilities;
    @SerializedName("categoryWise")
    @Expose
    private CategoryWise categoryWise;

    public String gethID() {
        return hID;
    }

    public void sethID(String hID) {
        this.hID = hID;
    }

    public DateFamily getDateFamily() {
        return dateFamily;
    }

    public void setDateFamily(DateFamily dateFamily) {
        this.dateFamily = dateFamily;
    }

    public PopulationInHouseHold getPopulationInHouseHold() {
        return populationInHouseHold;
    }

    public void setPopulationInHouseHold(PopulationInHouseHold populationInHouseHold) {
        this.populationInHouseHold = populationInHouseHold;
    }

    public PersonsWithDisabilities getPersonsWithDisabilities() {
        return personsWithDisabilities;
    }

    public void setPersonsWithDisabilities(PersonsWithDisabilities personsWithDisabilities) {
        this.personsWithDisabilities = personsWithDisabilities;
    }

    public CategoryWise getCategoryWise() {
        return categoryWise;
    }

    public void setCategoryWise(CategoryWise categoryWise) {
        this.categoryWise = categoryWise;
    }
    public class CategoryWise {

        @SerializedName("totalNumberOfGenerals")
        @Expose
        private Integer totalNumberOfGenerals;
        @SerializedName("totalNumberOfOBC")
        @Expose
        private Integer totalNumberOfOBC;
        @SerializedName("totalNumberOfSC")
        @Expose
        private Integer totalNumberOfSC;
        @SerializedName("totalNumberOfST")
        @Expose
        private Integer totalNumberOfST;

        public Integer getTotalNumberOfGenerals() {
            return totalNumberOfGenerals;
        }

        public void setTotalNumberOfGenerals(Integer totalNumberOfGenerals) {
            this.totalNumberOfGenerals = totalNumberOfGenerals;
        }

        public Integer getTotalNumberOfOBC() {
            return totalNumberOfOBC;
        }

        public void setTotalNumberOfOBC(Integer totalNumberOfOBC) {
            this.totalNumberOfOBC = totalNumberOfOBC;
        }

        public Integer getTotalNumberOfSC() {
            return totalNumberOfSC;
        }

        public void setTotalNumberOfSC(Integer totalNumberOfSC) {
            this.totalNumberOfSC = totalNumberOfSC;
        }

        public Integer getTotalNumberOfST() {
            return totalNumberOfST;
        }

        public void setTotalNumberOfST(Integer totalNumberOfST) {
            this.totalNumberOfST = totalNumberOfST;
        }

    }



    public class PersonsWithDisabilities {

        @SerializedName("totNoOfBlindLowVisionPersons")
        @Expose
        private Integer totNoOfBlindLowVisionPersons;
        @SerializedName("totNoOfHearingImpairedPersons")
        @Expose
        private Integer totNoOfHearingImpairedPersons;
        @SerializedName("totNoOfPhysicallyDisabled")
        @Expose
        private Integer totNoOfPhysicallyDisabled;
        @SerializedName("totNoOfLeprosyCuredPersons")
        @Expose
        private Integer totNoOfLeprosyCuredPersons;
        @SerializedName("totNoOfMentallyRetardedPersons")
        @Expose
        private Integer totNoOfMentallyRetardedPersons;
        @SerializedName("totNoOfPersonsMentallyIll")
        @Expose
        private Integer totNoOfPersonsMentallyIll;

        public Integer getTotNoOfBlindLowVisionPersons() {
            return totNoOfBlindLowVisionPersons;
        }

        public void setTotNoOfBlindLowVisionPersons(Integer totNoOfBlindLowVisionPersons) {
            this.totNoOfBlindLowVisionPersons = totNoOfBlindLowVisionPersons;
        }

        public Integer getTotNoOfHearingImpairedPersons() {
            return totNoOfHearingImpairedPersons;
        }

        public void setTotNoOfHearingImpairedPersons(Integer totNoOfHearingImpairedPersons) {
            this.totNoOfHearingImpairedPersons = totNoOfHearingImpairedPersons;
        }

        public Integer getTotNoOfPhysicallyDisabled() {
            return totNoOfPhysicallyDisabled;
        }

        public void setTotNoOfPhysicallyDisabled(Integer totNoOfPhysicallyDisabled) {
            this.totNoOfPhysicallyDisabled = totNoOfPhysicallyDisabled;
        }

        public Integer getTotNoOfLeprosyCuredPersons() {
            return totNoOfLeprosyCuredPersons;
        }

        public void setTotNoOfLeprosyCuredPersons(Integer totNoOfLeprosyCuredPersons) {
            this.totNoOfLeprosyCuredPersons = totNoOfLeprosyCuredPersons;
        }

        public Integer getTotNoOfMentallyRetardedPersons() {
            return totNoOfMentallyRetardedPersons;
        }

        public void setTotNoOfMentallyRetardedPersons(Integer totNoOfMentallyRetardedPersons) {
            this.totNoOfMentallyRetardedPersons = totNoOfMentallyRetardedPersons;
        }

        public Integer getTotNoOfPersonsMentallyIll() {
            return totNoOfPersonsMentallyIll;
        }

        public void setTotNoOfPersonsMentallyIll(Integer totNoOfPersonsMentallyIll) {
            this.totNoOfPersonsMentallyIll = totNoOfPersonsMentallyIll;
        }

    }
    public class DateFamily {

        @SerializedName("dayOfTheWeek")
        @Expose
        private Integer dayOfTheWeek;
        @SerializedName("month")
        @Expose
        private Integer month;
        @SerializedName("quarter")
        @Expose
        private Integer quarter;
        @SerializedName("year")
        @Expose
        private Integer year;
        @SerializedName("date")
        @Expose
        private String date;

        public Integer getDayOfTheWeek() {
            return dayOfTheWeek;
        }

        public void setDayOfTheWeek(Integer dayOfTheWeek) {
            this.dayOfTheWeek = dayOfTheWeek;
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Integer getQuarter() {
            return quarter;
        }

        public void setQuarter(Integer quarter) {
            this.quarter = quarter;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

    public class PopulationInHouseHold {

        @SerializedName("totalNumberOfMale")
        @Expose
        private Integer totalNumberOfMale;
        @SerializedName("totalNumberOfFemale")
        @Expose
        private Integer totalNumberOfFemale;
        @SerializedName("totalNumberOfMaleChildren")
        @Expose
        private Integer totalNumberOfMaleChildren;
        @SerializedName("totalNumberOfFemaleChildren")
        @Expose
        private Integer totalNumberOfFemaleChildren;
        @SerializedName("totalNumberOfThirdGender")
        @Expose
        private Integer totalNumberOfThirdGender;
        @SerializedName("totalNumberOfMembers")
        @Expose
        private Integer totalNumberOfMembers;

        public Integer getTotalNumberOfMale() {
            return totalNumberOfMale;
        }

        public void setTotalNumberOfMale(Integer totalNumberOfMale) {
            this.totalNumberOfMale = totalNumberOfMale;
        }

        public Integer getTotalNumberOfFemale() {
            return totalNumberOfFemale;
        }

        public void setTotalNumberOfFemale(Integer totalNumberOfFemale) {
            this.totalNumberOfFemale = totalNumberOfFemale;
        }

        public Integer getTotalNumberOfMaleChildren() {
            return totalNumberOfMaleChildren;
        }

        public void setTotalNumberOfMaleChildren(Integer totalNumberOfMaleChildren) {
            this.totalNumberOfMaleChildren = totalNumberOfMaleChildren;
        }

        public Integer getTotalNumberOfFemaleChildren() {
            return totalNumberOfFemaleChildren;
        }

        public void setTotalNumberOfFemaleChildren(Integer totalNumberOfFemaleChildren) {
            this.totalNumberOfFemaleChildren = totalNumberOfFemaleChildren;
        }

        public Integer getTotalNumberOfThirdGender() {
            return totalNumberOfThirdGender;
        }

        public void setTotalNumberOfThirdGender(Integer totalNumberOfThirdGender) {
            this.totalNumberOfThirdGender = totalNumberOfThirdGender;
        }

        public Integer getTotalNumberOfMembers() {
            return totalNumberOfMembers;
        }

        public void setTotalNumberOfMembers(Integer totalNumberOfMembers) {
            this.totalNumberOfMembers = totalNumberOfMembers;
        }

    }



}

