package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllPhcResponse {
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

    public static class Content implements Serializable {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("labels")
        @Expose
        private List<String> labels;
        @SerializedName("properties")
        @Expose
        private Properties properties;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }
    public static class Properties implements Serializable{

        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("contact")
        @Expose
        private long contact;
        @SerializedName("phc")
        @Expose
        private String phc;
        @SerializedName("phc_sub_center")
        @Expose
        private String phc_sub_center;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getContact() {
            return contact;
        }

        public void setContact(long contact) {
            this.contact = contact;
        }

        public String getPhc() {
            return phc;
        }

        public void setPhc(String phc) {
            this.phc = phc;
        }

        public String getPhc_sub_center() {
            return phc_sub_center;
        }

        public void setPhc_sub_center(String phc_sub_center) {
            this.phc_sub_center = phc_sub_center;
        }
    }

}
