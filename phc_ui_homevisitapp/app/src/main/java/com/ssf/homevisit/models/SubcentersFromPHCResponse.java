package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class SubcentersFromPHCResponse {
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<Content> content;

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

    public class Source implements Serializable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("labels")
        @Expose
        private List<String> labels = null;
        @SerializedName("properties")
        @Expose
        private PHCProperties properties;

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

        public PHCProperties getProperties() {
            return properties;
        }

        public void setProperties(PHCProperties properties) {
            this.properties = properties;
        }
    }

    public class Target implements Serializable{

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
    public static class SubCenterFromPhcComparator implements Comparator<SubcentersFromPHCResponse.Content> {
        @Override
        public int compare(SubcentersFromPHCResponse.Content content, SubcentersFromPHCResponse.Content t1) {
            return content.target.getProperties().getName().compareTo(t1.target.getProperties().getName());
        }
    }

}
