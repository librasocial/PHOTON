package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RMNCHAHouseHoldResponse {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<RMNCHAHouseHoldResponse.Content> content;

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

    public List<RMNCHAHouseHoldResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<RMNCHAHouseHoldResponse.Content> content) {
        this.content = content;
    }

    public class Content {
        @SerializedName("source")
        @Expose
        private RMNCHAHouseHoldResponse.Content.Source source;
        @SerializedName("target")
        @Expose
        private RMNCHAHouseHoldResponse.Content.Target target;

        public Content.Source getSource() {
            return source;
        }

        public void setSource(RMNCHAHouseHoldResponse.Content.Source source) {
            this.source = source;
        }

        public Content.Target getTarget() {
            return target;
        }

        public void setTarget(Content.Target target) {
            this.target = target;
        }


        public class Target {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private Content.Target.TargetProperties properties;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public List<String> getLabels() {
                return labels;
            }

            public void setLabels(List<String> labels) {
                this.labels = labels;
            }

            public Content.Target.TargetProperties getProperties() {
                return properties;
            }

            public void setProperties(Content.Target.TargetProperties properties) {
                this.properties = properties;
            }

            public class TargetProperties {

                @SerializedName("houseHeadName")
                @Expose
                private String houseHeadName;
                @SerializedName("dateCreated")
                @Expose
                private String dateCreated;
                @SerializedName("houseID")
                @Expose
                private String houseID;
                @SerializedName("totalFamilyMembers")
                @Expose
                private String totalFamilyMembers;
                @SerializedName("createdBy")
                @Expose
                private String createdBy;
                @SerializedName("latitude")
                @Expose
                private Double latitude;
                @SerializedName("dateModified")
                @Expose
                private String dateModified;
                @SerializedName("modifiedBy")
                @Expose
                private String modifiedBy;
                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("longitude")
                @Expose
                private Double longitude;

                public String getHouseHeadName() {
                    return houseHeadName;
                }

                public void setHouseHeadName(String houseHeadName) {
                    this.houseHeadName = houseHeadName;
                }

                public String getDateCreated() {
                    return dateCreated;
                }

                public void setDateCreated(String dateCreated) {
                    this.dateCreated = dateCreated;
                }

                public String getHouseID() {
                    return houseID;
                }

                public void setHouseID(String houseID) {
                    this.houseID = houseID;
                }

                public String getTotalFamilyMembers() {
                    return totalFamilyMembers;
                }

                public void setTotalFamilyMembers(String totalFamilyMembers) {
                    this.totalFamilyMembers = totalFamilyMembers;
                }

                public String getCreatedBy() {
                    return createdBy;
                }

                public void setCreatedBy(String createdBy) {
                    this.createdBy = createdBy;
                }

                public Double getLatitude() {
                    return latitude;
                }

                public void setLatitude(Double latitude) {
                    this.latitude = latitude;
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

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public Double getLongitude() {
                    return longitude;
                }

                public void setLongitude(Double longitude) {
                    this.longitude = longitude;
                }


            }

        }

        public class Source {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private Content.Source.SourceProperties properties;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public List<String> getLabels() {
                return labels;
            }

            public void setLabels(List<String> labels) {
                this.labels = labels;
            }

            public Content.Source.SourceProperties getProperties() {
                return properties;
            }

            public void setProperties(Content.Source.SourceProperties sourceProperties) {
                this.properties = sourceProperties;
            }

            public class SourceProperties {

                @SerializedName("lgdCode")
                @Expose
                private String lgdCode;
                @SerializedName("dateCreated")
                @Expose
                private String dateCreated;
                @SerializedName("createdBy")
                @Expose
                private String createdBy;
                @SerializedName("name")
                @Expose
                private String name;
                @SerializedName("dateModified")
                @Expose
                private String dateModified;
                @SerializedName("modifiedBy")
                @Expose
                private String modifiedBy;
                @SerializedName("houseHoldCount")
                @Expose
                private String houseHoldCount;
                @SerializedName("uuid")
                @Expose
                private String uuid;

                public String getLgdCode() {
                    return lgdCode;
                }

                public void setLgdCode(String lgdCode) {
                    this.lgdCode = lgdCode;
                }

                public String getDateCreated() {
                    return dateCreated;
                }

                public void setDateCreated(String dateCreated) {
                    this.dateCreated = dateCreated;
                }

                public String getCreatedBy() {
                    return createdBy;
                }

                public void setCreatedBy(String createdBy) {
                    this.createdBy = createdBy;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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

                public String getHouseHoldCount() {
                    return houseHoldCount;
                }

                public void setHouseHoldCount(String houseHoldCount) {
                    this.houseHoldCount = houseHoldCount;
                }

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

            }
        }
    }
}
