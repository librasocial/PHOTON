package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RMNCHAPNCHouseholdsResponse {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<RMNCHAPNCHouseholdsResponse.Content> content;

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

    public List<RMNCHAPNCHouseholdsResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<RMNCHAPNCHouseholdsResponse.Content> content) {
        this.content = content;
    }

    public class Content {
        @SerializedName("relationship")
        @Expose
        private RMNCHAPNCHouseholdsResponse.Content.Relationship relationship;
        @SerializedName("sourceNode")
        @Expose
        private RMNCHAPNCHouseholdsResponse.Content.Source source;
        @SerializedName("targetNode")
        @Expose
        private RMNCHAPNCHouseholdsResponse.Content.Target target;


        public Relationship getRelationship() {
            return relationship;
        }

        public void setRelationship(Relationship relationship) {
            this.relationship = relationship;
        }

        public RMNCHAPNCHouseholdsResponse.Content.Source getSource() {
            return source;
        }

        public void setSource(RMNCHAPNCHouseholdsResponse.Content.Source source) {
            this.source = source;
        }

        public RMNCHAPNCHouseholdsResponse.Content.Target getTarget() {
            return target;
        }

        public void setTarget(RMNCHAPNCHouseholdsResponse.Content.Target target) {
            this.target = target;
        }



        public class Relationship implements Serializable {
            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("properties")
            @Expose
            private RMNCHAPNCHouseholdsResponse.Content.Relationship.RelationshipProperties properties;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public RMNCHAPNCHouseholdsResponse.Content.Relationship.RelationshipProperties getProperties() {
                return properties;
            }

            public void setProperties(RMNCHAPNCHouseholdsResponse.Content.Relationship.RelationshipProperties properties) {
                this.properties = properties;
            }

            public class RelationshipProperties implements Serializable {

            }
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
            private RMNCHAPNCHouseholdsResponse.Content.Target.TargetProperties properties;

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

            public RMNCHAPNCHouseholdsResponse.Content.Target.TargetProperties getProperties() {
                return properties;
            }

            public void setProperties(RMNCHAPNCHouseholdsResponse.Content.Target.TargetProperties properties) {
                this.properties = properties;
            }

            public class TargetProperties {

                @SerializedName("dateCreated")
                @Expose
                private String dateCreated;
                @SerializedName("lgdCode")
                @Expose
                private String lgdCode;
                @SerializedName("totalPopulation")
                @Expose
                private int totalPopulation;
                @SerializedName("createdBy")
                @Expose
                private String createdBy;
                @SerializedName("name")
                @Expose
                private String name;
                @SerializedName("modifiedBy")
                @Expose
                private String modifiedBy;
                @SerializedName("dateModified")
                @Expose
                private String dateModified;
                @SerializedName("houseHoldCount")
                @Expose
                private int houseHoldCount;
                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("type")
                @Expose
                private String type;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getDateCreated() {
                    return dateCreated;
                }

                public void setDateCreated(String dateCreated) {
                    this.dateCreated = dateCreated;
                }

                public String getLgdCode() {
                    return lgdCode;
                }

                public void setLgdCode(String lgdCode) {
                    this.lgdCode = lgdCode;
                }

                public int getTotalPopulation() {
                    return totalPopulation;
                }

                public void setTotalPopulation(int totalPopulation) {
                    this.totalPopulation = totalPopulation;
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

                public String getModifiedBy() {
                    return modifiedBy;
                }

                public void setModifiedBy(String modifiedBy) {
                    this.modifiedBy = modifiedBy;
                }

                public String getDateModified() {
                    return dateModified;
                }

                public void setDateModified(String dateModified) {
                    this.dateModified = dateModified;
                }

                public int getHouseHoldCount() {
                    return houseHoldCount;
                }

                public void setHouseHoldCount(int houseHoldCount) {
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

        public class Source {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private RMNCHAPNCHouseholdsResponse.Content.Source.SourceProperties properties;

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

            public RMNCHAPNCHouseholdsResponse.Content.Source.SourceProperties getProperties() {
                return properties;
            }

            public void setProperties(RMNCHAPNCHouseholdsResponse.Content.Source.SourceProperties sourceProperties) {
                this.properties = sourceProperties;
            }

            public class SourceProperties {

                @SerializedName("houseID")
                @Expose
                private String houseID;
                @SerializedName("hasEligibleCouple")
                @Expose
                private boolean hasEligibleCouple;
                @SerializedName("pregnantLadyName")
                @Expose
                private String pregnantLadyName;
                @SerializedName("latitude")
                @Expose
                private double latitude;
                @SerializedName("dateModified")
                @Expose
                private String dateModified;
                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("houseHeadName")
                @Expose
                private String houseHeadName;
                @SerializedName("dateCreated")
                @Expose
                private String dateCreated;
                @SerializedName("totalFamilyMembers")
                @Expose
                private Integer totalFamilyMembers;
                @SerializedName("createdBy")
                @Expose
                private String createdBy;
                @SerializedName("houseHeadImageUrls")
                @Expose
                private List<String> houseHeadImageUrls;
                @SerializedName("hasPregnantWoman")
                @Expose
                private boolean hasPregnantWoman;
                @SerializedName("modifiedBy")
                @Expose
                private String modifiedBy;
                @SerializedName("longitude")
                @Expose
                private double longitude;

                public String getHouseID() {
                    return houseID;
                }

                public void setHouseID(String houseID) {
                    this.houseID = houseID;
                }

                public boolean isHasEligibleCouple() {
                    return hasEligibleCouple;
                }

                public void setHasEligibleCouple(boolean hasEligibleCouple) {
                    this.hasEligibleCouple = hasEligibleCouple;
                }

                public String getPregnantLadyName() {
                    return pregnantLadyName;
                }

                public void setPregnantLadyName(String pregnantLadyName) {
                    this.pregnantLadyName = pregnantLadyName;
                }

                public double getLatitude() {
                    return latitude;
                }

                public void setLatitude(double latitude) {
                    this.latitude = latitude;
                }

                public String getDateModified() {
                    return dateModified;
                }

                public void setDateModified(String dateModified) {
                    this.dateModified = dateModified;
                }

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

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

                public Integer getTotalFamilyMembers() {
                    return totalFamilyMembers;
                }

                public void setTotalFamilyMembers(Integer totalFamilyMembers) {
                    this.totalFamilyMembers = totalFamilyMembers;
                }

                public String getCreatedBy() {
                    return createdBy;
                }

                public void setCreatedBy(String createdBy) {
                    this.createdBy = createdBy;
                }

                public List<String> getHouseHeadImageUrls() {
                    return houseHeadImageUrls;
                }

                public void setHouseHeadImageUrls(List<String> houseHeadImageUrls) {
                    this.houseHeadImageUrls = houseHeadImageUrls;
                }

                public boolean isHasPregnantWoman() {
                    return hasPregnantWoman;
                }

                public void setHasPregnantWoman(boolean hasPregnantWoman) {
                    this.hasPregnantWoman = hasPregnantWoman;
                }

                public String getModifiedBy() {
                    return modifiedBy;
                }

                public void setModifiedBy(String modifiedBy) {
                    this.modifiedBy = modifiedBy;
                }

                public double getLongitude() {
                    return longitude;
                }

                public void setLongitude(double longitude) {
                    this.longitude = longitude;
                }
            }
        }

    }



}
