package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HouseHoldInVillageResponse {
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<HouseHoldInVillageResponse.Content> content;

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

    public List<HouseHoldInVillageResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<HouseHoldInVillageResponse.Content> content) {
        this.content = content;
    }

    public class Content {

        @SerializedName("source")
        @Expose
        private HouseHoldInVillageResponse.Source source;
        @SerializedName("target")
        @Expose
        private HouseHoldInVillageResponse.Target target;

        public HouseHoldInVillageResponse.Source getSource() {
            return source;
        }

        public void setSource(HouseHoldInVillageResponse.Source source) {
            this.source = source;
        }

        public HouseHoldInVillageResponse.Target getTarget() {
            return target;
        }

        public void setTarget(HouseHoldInVillageResponse.Target target) {
            this.target = target;
        }


    }

    public class Source {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("labels")
        @Expose
        private List<String> labels = null;
        @SerializedName("properties")
        @Expose
        private VillageProperties properties;

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

        public VillageProperties getProperties() {
            return properties;
        }

        public void setProperties(VillageProperties properties) {
            this.properties = properties;
        }

    }

    public class Target {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("labels")
        @Expose
        private List<String> labels = null;
        @SerializedName("properties")
        @Expose
        private HouseHoldProperties properties;

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

        public HouseHoldProperties getProperties() {
            return properties;
        }

        public void setProperties(HouseHoldProperties properties) {
            this.properties = properties;
        }

    }

}
