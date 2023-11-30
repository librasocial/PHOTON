package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResidentInHouseHoldResponse {
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<ResidentInHouseHoldResponse.Content> content;

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

    public class Content {

            @SerializedName("sourceNode")
            @Expose
            private ResidentInHouseHoldResponse.Source source;
            @SerializedName("targetNode")
            @Expose
            private ResidentInHouseHoldResponse.Target target;
            @SerializedName("relationshipType")

            private Boolean isChecked; // client variable only
            private Boolean inviteSent; // client variable only

        public void setInviteSent(Boolean inviteSent) {
            this.inviteSent = inviteSent;
        }

        public Boolean getInviteSent() {
            return inviteSent;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }

        public Boolean getChecked() {
            return isChecked;
        }

        public ResidentInHouseHoldResponse.Source getSource() {
                return source;
            }

            public void setSource(ResidentInHouseHoldResponse.Source source) {
                this.source = source;
            }

            public ResidentInHouseHoldResponse.Target getTarget() {
                return target;
            }

            public void setTarget(ResidentInHouseHoldResponse.Target target) {
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

    public class Target {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("labels")
        @Expose
        private List<String> labels = null;
        @SerializedName("properties")
        @Expose
        private ResidentProperties properties;

        private String headName;

        public void setHeadName(String headName) {
            this.headName = headName;
        }

        public String getHeadName() {
            return headName;
        }

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

        public ResidentProperties getProperties() {
            return properties;
        }

        public void setProperties(ResidentProperties properties) {
            this.properties = properties;
        }

    }

}
