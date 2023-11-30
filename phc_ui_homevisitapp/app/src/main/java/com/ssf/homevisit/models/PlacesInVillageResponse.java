package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesInVillageResponse {
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<PlacesInVillageResponse.Content> content = null;

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

    public List<PlacesInVillageResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<PlacesInVillageResponse.Content> content) {
        this.content = content;
    }

    public static class Content {

        @SerializedName("source")
        @Expose
        private PlacesInVillageResponse.Source source;
        @SerializedName("target")
        @Expose
        private PlacesInVillageResponse.Target target;


        private Boolean isSelected;

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }

        public PlacesInVillageResponse.Source getSource() {
            return source;
        }

        public void setSource(PlacesInVillageResponse.Source source) {
            this.source = source;
        }

        public PlacesInVillageResponse.Target getTarget() {
            return target;
        }

        public void setTarget(PlacesInVillageResponse.Target target) {
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

    public static class Target {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("labels")
        @Expose
        private List<String> labels = null;
        @SerializedName("properties")
        @Expose
        private PlaceProperties properties;

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

        public PlaceProperties getProperties() {
            return properties;
        }

        public void setProperties(PlaceProperties properties) {
            this.properties = properties;
        }

    }

}
