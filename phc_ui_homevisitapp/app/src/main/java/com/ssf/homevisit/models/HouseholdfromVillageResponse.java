package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HouseholdfromVillageResponse {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<HouseholdfromVillageResponse.Content> content = null;

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


        public class Target {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private TargetProperties properties;

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

            public TargetProperties getProperties() {
                return properties;
            }

            public void setProperties(TargetProperties properties) {
                this.properties = properties;
            }

            public class TargetProperties {

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
                private String latitude;
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
                private String longitude;

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

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
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

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
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
            private SourceProperties properties;

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

            public SourceProperties getProperties() {
                return properties;
            }

            public void setProperties(SourceProperties sourceProperties) {
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
