package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RMNCHAMembersInHouseHoldResponse {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<RMNCHAMembersInHouseHoldResponse.Content> content;

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

    public List<RMNCHAMembersInHouseHoldResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<RMNCHAMembersInHouseHoldResponse.Content> content) {
        this.content = content;
    }

    public class Content implements Serializable {

        @SerializedName("relationshipType")
        @Expose
        private String relationshipType;
        @SerializedName("sourceNode")
        @Expose
        private RMNCHAMembersInHouseHoldResponse.Content.SourceNode sourceNode;
        @SerializedName("targetNode")
        @Expose
        private RMNCHAMembersInHouseHoldResponse.Content.TargetNode targetNode;

        public String getRelationshipType() {
            return relationshipType;
        }

        public void setRelationshipType(String relationshipType) {
            this.relationshipType = relationshipType;
        }

        public SourceNode getSourceNode() {
            return sourceNode;
        }

        public void setSourceNode(SourceNode sourceNode) {
            this.sourceNode = sourceNode;
        }

        public TargetNode getTargetNode() {
            return targetNode;
        }

        public void setTargetNode(TargetNode targetNode) {
            this.targetNode = targetNode;
        }

        public class SourceNode implements Serializable {

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

            public class SourceProperties implements Serializable {

                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("houseID")
                @Expose
                private String houseID;
                @SerializedName("totalFamilyMembers")
                @Expose
                private double totalFamilyMembers;
                @SerializedName("houseHeadName")
                @Expose
                private String houseHeadName;
                @SerializedName("dateCreated")
                @Expose
                private String dateCreated;
                @SerializedName("createdBy")
                @Expose
                private String createdBy;
                @SerializedName("dateModified")
                @Expose
                private String dateModified;
                @SerializedName("modifiedBy")
                @Expose
                private String modifiedBy;
                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("latitude")
                @Expose
                private double latitude;
                @SerializedName("longitude")
                @Expose
                private double longitude;
                @SerializedName("hasEligibleCouple")
                @Expose
                private boolean hasEligibleCouple;

                public double getLatitude() {
                    return latitude;
                }

                public void setLatitude(double latitude) {
                    this.latitude = latitude;
                }

                public double getLongitude() {
                    return longitude;
                }

                public void setLongitude(double longitude) {
                    this.longitude = longitude;
                }

                public boolean isHasEligibleCouple() {
                    return hasEligibleCouple;
                }

                public void setHasEligibleCouple(boolean hasEligibleCouple) {
                    this.hasEligibleCouple = hasEligibleCouple;
                }

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public String getHouseID() {
                    return houseID;
                }

                public void setHouseID(String houseID) {
                    this.houseID = houseID;
                }

                public double getTotalFamilyMembers() {
                    return totalFamilyMembers;
                }

                public void setTotalFamilyMembers(double totalFamilyMembers) {
                    this.totalFamilyMembers = totalFamilyMembers;
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

                public String getCreatedBy() {
                    return createdBy;
                }

                public void setCreatedBy(String createdBy) {
                    this.createdBy = createdBy;
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

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }

        public class TargetNode implements Serializable {

            @SerializedName("id")
            @Expose
            private int id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private TargetProperties properties;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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

            public class TargetProperties implements Serializable {

                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("healthID")
                @Expose
                private String healthID;
                @SerializedName("firstName")
                @Expose
                private String firstName;
                @SerializedName("middleName")
                @Expose
                private String middleName;
                @SerializedName("lastName")
                @Expose
                private String lastName;
                @SerializedName("dateOfBirth")
                @Expose
                private String dateOfBirth;
                @SerializedName("age")
                @Expose
                private Integer age;
                @SerializedName("gender")
                @Expose
                private String gender;
                @SerializedName("isHead")
                @Expose
                private boolean isHead;
                @SerializedName("contact")
                @Expose
                private String contact;
                @SerializedName("residingInVillage")
                @Expose
                private String residingInVillage;
                @SerializedName("imageUrls")
                @Expose
                private List<String> imageUrls;
                @SerializedName("dateCreated")
                @Expose
                private String dateCreated;
                @SerializedName("createdBy")
                @Expose
                private String createdBy;
                @SerializedName("dateModified")
                @Expose
                private String dateModified;
                @SerializedName("modifiedBy")
                @Expose
                private String modifiedBy;
                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("rchId")
                @Expose
                private String rchId;
                @SerializedName("rmnchaServiceStatus")
                @Expose
                private RMNCHAServiceStatus rmnchaServiceStatus;
                @SerializedName("ecServiceId")
                @Expose
                private String serviceId;

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public String getHealthID() {
                    return healthID;
                }

                public void setHealthID(String healthID) {
                    this.healthID = healthID;
                }

                public String getFirstName() {
                    return firstName;
                }

                public void setFirstName(String firstName) {
                    this.firstName = firstName;
                }

                public String getMiddleName() {
                    return middleName;
                }

                public void setMiddleName(String middleName) {
                    this.middleName = middleName;
                }

                public String getLastName() {
                    return lastName;
                }

                public void setLastName(String lastName) {
                    this.lastName = lastName;
                }

                public String getDateOfBirth() {
                    return dateOfBirth;
                }

                public void setDateOfBirth(String dateOfBirth) {
                    this.dateOfBirth = dateOfBirth;
                }

                public Integer getAge() {
                    return age;
                }

                public void setAge(Integer age) {
                    this.age = age;
                }

                public String getGender() {
                    return gender;
                }

                public void setGender(String gender) {
                    this.gender = gender;
                }

                public boolean isHead() {
                    return isHead;
                }

                public void setHead(boolean head) {
                    isHead = head;
                }

                public String getContact() {
                    return contact;
                }

                public void setContact(String contact) {
                    this.contact = contact;
                }

                public String getResidingInVillage() {
                    return residingInVillage;
                }

                public void setResidingInVillage(String residingInVillage) {
                    this.residingInVillage = residingInVillage;
                }

                public List<String> getImageUrls() {
                    return imageUrls;
                }

                public void setImageUrls(List<String> imageUrls) {
                    this.imageUrls = imageUrls;
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

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getRchId() {
                    return rchId;
                }

                public void setRchId(String rchId) {
                    this.rchId = rchId;
                }

                public RMNCHAServiceStatus getRMNCHAServiceStatus() {
                    return rmnchaServiceStatus;
                }

                public void setRMNCHAServiceStatus(RMNCHAServiceStatus serviceType) {
                    this.rmnchaServiceStatus = serviceType;
                }

                public String getServiceId() {
                    return serviceId;
                }

                public void setServiceId(String serviceId) {
                    this.serviceId = serviceId;
                }
            }

        }

    }

}
