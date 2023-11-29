package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RMNCHACoupleDetailsResponse {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<RMNCHACoupleDetailsResponse.Content> content;

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

    public List<RMNCHACoupleDetailsResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<RMNCHACoupleDetailsResponse.Content> content) {
        this.content = content;
    }

    public class Content {
        @SerializedName("relationship")
        @Expose
        private RMNCHACoupleDetailsResponse.Content.Relationship relationship;
        @SerializedName("sourceNode")
        @Expose
        private RMNCHACoupleDetailsResponse.Content.Source sourceNode;
        @SerializedName("targetNode")
        @Expose
        private RMNCHACoupleDetailsResponse.Content.Target targetNode;

        public Relationship getRelationship() {
            return relationship;
        }

        public void setRelationship(Relationship relationship) {
            this.relationship = relationship;
        }

        public RMNCHACoupleDetailsResponse.Content.Source getSource() {
            return sourceNode;
        }

        public void setSource(RMNCHACoupleDetailsResponse.Content.Source source) {
            this.sourceNode = source;
        }

        public RMNCHACoupleDetailsResponse.Content.Target getTarget() {
            return targetNode;
        }

        public void setTarget(RMNCHACoupleDetailsResponse.Content.Target target) {
            this.targetNode = target;
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
            private RMNCHACoupleDetailsResponse.Content.Target.TargetProperties properties;

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

            public RMNCHACoupleDetailsResponse.Content.Target.TargetProperties getProperties() {
                return properties;
            }

            public void setProperties(RMNCHACoupleDetailsResponse.Content.Target.TargetProperties properties) {
                this.properties = properties;
            }

            public class TargetProperties {
                @SerializedName("uuid")
                @Expose
                private String uuid;
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
                private double age;
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
                @SerializedName("healthID")
                @Expose
                private String healthID;

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

                public String getHealthID() {
                    return healthID;
                }

                public void setHealthID(String healthID) {
                    this.healthID = healthID;
                }

            }

        }

        public class Relationship {
            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("properties")
            @Expose
            private RMNCHACoupleDetailsResponse.Content.Relationship.RelationshipProperties properties;

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

            public RelationshipProperties getProperties() {
                return properties;
            }

            public void setProperties(RelationshipProperties properties) {
                this.properties = properties;
            }

            public class RelationshipProperties {
                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("rchId")
                @Expose
                private String rchId;
                @SerializedName("serialNumber")
                @Expose
                private long serialNumber;
                @SerializedName("ecRegistrationId")
                @Expose
                private String ecRegistrationId;
                @SerializedName("ancServiceId")
                @Expose
                private String ancServiceId;
                @SerializedName("ancRegistrationId")
                @Expose
                private String ancRegistrationId;
                @SerializedName("pncRegistrationId")
                @Expose
                private String pncRegistrationId;
                @SerializedName("pncServiceId")
                @Expose
                private String pncServiceId;
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

                public long getSerialNumber() {
                    return serialNumber;
                }

                public void setSerialNumber(long serialNumber) {
                    this.serialNumber = serialNumber;
                }

                public String getEcRegistrationId() {
                    return ecRegistrationId;
                }

                public void setEcRegistrationId(String ecRegistrationId) {
                    this.ecRegistrationId = ecRegistrationId;
                }

                public String getAncServiceId() {
                    return ancServiceId;
                }

                public void setAncServiceId(String ancServiceId) {
                    this.ancServiceId = ancServiceId;
                }

                public String getAncRegistrationId() {
                    return ancRegistrationId;
                }

                public void setAncRegistrationId(String ancRegistrationId) {
                    this.ancRegistrationId = ancRegistrationId;
                }

                public String getPncRegistrationId() {
                    return pncRegistrationId;
                }

                public void setPncRegistrationId(String pncRegistrationId) {
                    this.pncRegistrationId = pncRegistrationId;
                }

                public String getPncServiceId() {
                    return pncServiceId;
                }

                public void setPncServiceId(String pncServiceId) {
                    this.pncServiceId = pncServiceId;
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

        public class Source {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private RMNCHACoupleDetailsResponse.Content.Source.SourceProperties properties;

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

            public RMNCHACoupleDetailsResponse.Content.Source.SourceProperties getProperties() {
                return properties;
            }

            public void setProperties(RMNCHACoupleDetailsResponse.Content.Source.SourceProperties sourceProperties) {
                this.properties = sourceProperties;
            }

            public class SourceProperties {

                @SerializedName("uuid")
                @Expose
                private String uuid;
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
                private String age;
                @SerializedName("gender")
                @Expose
                private String gender;
                @SerializedName("isHead")
                @Expose
                private boolean isHead;
                @SerializedName("isAdult")
                @Expose
                private boolean isAdult;
                @SerializedName("isPregnant")
                @Expose
                private boolean isPregnant;
                @SerializedName("contact")
                @Expose
                private String contact;
                @SerializedName("rchId")
                @Expose
                private String rchId;
                @SerializedName("rmnchaServiceStatus")
                @Expose
                private RMNCHAServiceStatus rmnchaServiceStatus;
                @SerializedName("residingInVillage")
                @Expose
                private String residingInVillage;
                @SerializedName("imageUrls")
                @Expose
                private List<String> imageUrls;
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
                @SerializedName("healthID")
                @Expose
                private String healthID;
                @SerializedName("latitude")
                @Expose
                private double latitude;
                @SerializedName("longitude")
                @Expose
                private double longitude;

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

                public String getHealthID() {
                    return healthID;
                }

                public void setHealthID(String healthID) {
                    this.healthID = healthID;
                }

                public boolean isAdult() {
                    return isAdult;
                }

                public void setAdult(boolean adult) {
                    isAdult = adult;
                }

                public String getDateOfBirth() {
                    return dateOfBirth;
                }

                public void setDateOfBirth(String dateOfBirth) {
                    this.dateOfBirth = dateOfBirth;
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

                public String getAge() {
                    return age;
                }

                public void setAge(String age) {
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

                public boolean isPregnant() {
                    return isPregnant;
                }

                public void setPregnant(boolean pregnant) {
                    isPregnant = pregnant;
                }

                public String getContact() {
                    return contact;
                }

                public void setContact(String contact) {
                    this.contact = contact;
                }

                public String getRchId() {
                    return rchId;
                }

                public void setRchId(String rchId) {
                    this.rchId = rchId;
                }

                public RMNCHAServiceStatus getRmnchaServiceStatus() {
                    return rmnchaServiceStatus;
                }

                public void setRmnchaServiceStatus(RMNCHAServiceStatus rmnchaServiceStatus) {
                    this.rmnchaServiceStatus = rmnchaServiceStatus;
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


}
