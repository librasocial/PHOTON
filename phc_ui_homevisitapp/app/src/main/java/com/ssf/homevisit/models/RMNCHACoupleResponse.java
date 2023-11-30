package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RMNCHACoupleResponse {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<RMNCHACoupleResponse.Content> content;

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

    public List<RMNCHACoupleResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<RMNCHACoupleResponse.Content> content) {
        this.content = content;
    }

    public class Content implements Serializable {

        @SerializedName("relationship")
        @Expose
        private RMNCHACoupleResponse.Content.RelationShipNode relationship;
        @SerializedName("sourceNode")
        @Expose
        private RMNCHACoupleResponse.Content.MemberNode sourceNode;
        @SerializedName("targetNode")
        @Expose
        private RMNCHACoupleResponse.Content.MemberNode targetNode;

        public RMNCHACoupleResponse.Content.RelationShipNode getRelationship() {
            return relationship;
        }

        public void setRelationship(RMNCHACoupleResponse.Content.RelationShipNode relationship) {
            this.relationship = relationship;
        }

        public MemberNode getSourceNode() {
            return sourceNode;
        }

        public void setSourceNode(MemberNode sourceNode) {
            this.sourceNode = sourceNode;
        }

        public MemberNode getTargetNode() {
            return targetNode;
        }

        public void setTargetNode(MemberNode targetNode) {
            this.targetNode = targetNode;
        }

        public class RelationShipNode implements Serializable {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("properties")
            @Expose
            private RelationShipProperties properties;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public RelationShipProperties getProperties() {
                return properties;
            }

            public void setProperties(RelationShipProperties sourceProperties) {
                this.properties = sourceProperties;
            }

            public class RelationShipProperties implements Serializable {

                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("rchId")
                @Expose
                private String rchId;
                @SerializedName("serialNumber")
                @Expose
                private double serialNumber;
                @SerializedName("registeredBy")
                @Expose
                private String registeredBy;
                @SerializedName("registeredByName")
                @Expose
                private String registeredByName;
                @SerializedName("registeredOn")
                @Expose
                private String registeredOn;
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
                @SerializedName("ecServiceId")
                @Expose
                private String serviceId;

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public String getRchId() {
                    return rchId;
                }

                public void setRchId(String rchId) {
                    this.rchId = rchId;
                }

                public double getSerialNumber() {
                    return serialNumber;
                }

                public void setSerialNumber(double serialNumber) {
                    this.serialNumber = serialNumber;
                }

                public String getRegisteredBy() {
                    return registeredBy;
                }

                public void setRegisteredBy(String registeredBy) {
                    this.registeredBy = registeredBy;
                }

                public String getRegisteredByName() {
                    return registeredByName;
                }

                public void setRegisteredByName(String registeredByName) {
                    this.registeredByName = registeredByName;
                }

                public String getRegisteredOn() {
                    return registeredOn;
                }

                public void setRegisteredOn(String registeredOn) {
                    this.registeredOn = registeredOn;
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

                public String getServiceId() {
                    return serviceId;
                }

                public void setServiceId(String serviceId) {
                    this.serviceId = serviceId;
                }
            }
        }

        public class MemberNode implements Serializable {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private Properties properties;

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

            public Properties getProperties() {
                return properties;
            }

            public void setProperties(Properties sourceProperties) {
                this.properties = sourceProperties;
            }

            public class Properties implements Serializable {

                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("firstName")
                @Expose
                private String firstName;
                @SerializedName("age")
                @Expose
                private double age;
                @SerializedName("gender")
                @Expose
                private String gender;
                @SerializedName("imageUrls")
                @Expose
                private List<String> imageUrls;
                @SerializedName("isHead")
                @Expose
                private String isHead;
                @SerializedName("rchId")
                @Expose
                private String rchId;
                @SerializedName("residingInVillage")
                @Expose
                private String residingInVillage;
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
                @SerializedName("dateOfBirth")
                @Expose
                private String dateOfBirth;
                @SerializedName("contact")
                @Expose
                private String contact;
                @SerializedName("healthID")
                @Expose
                private String healthID;

                public String getDateOfBirth() {
                    return dateOfBirth;
                }

                public void setDateOfBirth(String dateOfBirth) {
                    this.dateOfBirth = dateOfBirth;
                }

                public String getContact() {
                    return contact;
                }

                public void setContact(String contact) {
                    this.contact = contact;
                }

                public String getHealthID() {
                    return healthID;
                }

                public void setHealthID(String healthID) {
                    this.healthID = healthID;
                }

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public String getFirstName() {
                    return firstName;
                }

                public void setFirstName(String firstName) {
                    this.firstName = firstName;
                }

                public double getAge() {
                    return age;
                }

                public void setAge(double age) {
                    this.age = age;
                }

                public String getGender() {
                    return gender;
                }

                public void setGender(String gender) {
                    this.gender = gender;
                }

                public List<String> getImageUrls() {
                    return imageUrls;
                }

                public void setImageUrls(List<String> imageUrls) {
                    this.imageUrls = imageUrls;
                }

                public String getIsHead() {
                    return isHead;
                }

                public void setIsHead(String isHead) {
                    this.isHead = isHead;
                }

                public String getRchId() {
                    return rchId;
                }

                public void setRchId(String rchId) {
                    this.rchId = rchId;
                }

                public String getResidingInVillage() {
                    return residingInVillage;
                }

                public void setResidingInVillage(String residingInVillage) {
                    this.residingInVillage = residingInVillage;
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

    }

    public enum EC_SERVICE_TYPE{
        NEW,
        EC_REGISTERED,
        BC_ONGOING,
        PC_ONGOING
    }

}
