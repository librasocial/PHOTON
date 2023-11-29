package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class SubCVillResponse {
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<Content> content = null;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public class Content implements Serializable {

        @SerializedName("source")
        @Expose
        private Source source;
        @SerializedName("target")
        @Expose
        private Target target;

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public Target getTarget() {
            return target;
        }

        public void setTarget(Target target) {
            this.target = target;
        }

    }

    public class Source implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("labels")
        @Expose
        private List<String> labels = null;
        @SerializedName("properties")
        @Expose
        private SubcenterProperties properties;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public SubcenterProperties getProperties() {
            return properties;
        }

        public void setProperties(SubcenterProperties properties) {
            this.properties = properties;
        }

    }

    public class Target implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("labels")
        @Expose
        private List<String> labels = null;
        @SerializedName("properties")
        @Expose
        private VillageProperties villageProperties;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public VillageProperties getVillageProperties() {
            return villageProperties;
        }

        public void setVillageProperties(VillageProperties villageProperties) {
            this.villageProperties = villageProperties;
        }

    }

    public class Properties {

        @SerializedName("out_of_area_count")
        @Expose
        private Integer outOfAreaCount;
        @SerializedName("in_area_count")
        @Expose
        private Integer inAreaCount;
        @SerializedName("household_count")
        @Expose
        private Integer householdCount;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("total_population")
        @Expose
        private Integer totalPopulation;
        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("lgd_code")
        @Expose
        private String lgdCode;
        @SerializedName("high_risk_area_count")
        @Expose
        private Integer highRiskAreaCount;

        public Integer getOutOfAreaCount() {
            return outOfAreaCount;
        }

        public void setOutOfAreaCount(Integer outOfAreaCount) {
            this.outOfAreaCount = outOfAreaCount;
        }

        public Integer getInAreaCount() {
            return inAreaCount;
        }

        public void setInAreaCount(Integer inAreaCount) {
            this.inAreaCount = inAreaCount;
        }

        public Integer getHouseholdCount() {
            return householdCount;
        }

        public void setHouseholdCount(Integer householdCount) {
            this.householdCount = householdCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getTotalPopulation() {
            return totalPopulation;
        }

        public void setTotalPopulation(Integer totalPopulation) {
            this.totalPopulation = totalPopulation;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getLgdCode() {
            return lgdCode;
        }

        public void setLgdCode(String lgdCode) {
            this.lgdCode = lgdCode;
        }

        public Integer getHighRiskAreaCount() {
            return highRiskAreaCount;
        }

        public void setHighRiskAreaCount(Integer highRiskAreaCount) {
            this.highRiskAreaCount = highRiskAreaCount;
        }

    }
    public static class SubCVillComparator implements Comparator<Content> {
        @Override
        public int compare(Content content, Content t1) {
            return content.target.villageProperties.getName().compareTo(t1.target.villageProperties.getName());
        }
    }
}