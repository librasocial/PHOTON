package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RMNCHAPNCWomenResponse implements Serializable {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<RMNCHAPNCWomenResponse.Content> content;

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

    public List<RMNCHAPNCWomenResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<RMNCHAPNCWomenResponse.Content> content) {
        this.content = content;
    }

    public class Content implements Serializable {
        @SerializedName("relationship")
        @Expose
        private RMNCHAPNCWomenResponse.Content.Relationship relationship;
        @SerializedName("sourceNode")
        @Expose
        private RMNCHAPNCWomenResponse.Content.Source sourceNode;
        @SerializedName("targetNode")
        @Expose
        private RMNCHAPNCWomenResponse.Content.Target targetNode;

        public Relationship getRelationship() {
            return relationship;
        }

        public void setRelationship(Relationship relationship) {
            this.relationship = relationship;
        }

        public RMNCHAPNCWomenResponse.Content.Source getSource() {
            return sourceNode;
        }

        public void setSource(RMNCHAPNCWomenResponse.Content.Source source) {
            this.sourceNode = source;
        }

        public RMNCHAPNCWomenResponse.Content.Target getTarget() {
            return targetNode;
        }

        public void setTarget(RMNCHAPNCWomenResponse.Content.Target target) {
            this.targetNode = target;
        }


        public class Target implements Serializable {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private RMNCHAPNCWomenResponse.Content.Target.TargetProperties properties;

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

            public RMNCHAPNCWomenResponse.Content.Target.TargetProperties getProperties() {
                return properties;
            }

            public void setProperties(RMNCHAPNCWomenResponse.Content.Target.TargetProperties properties) {
                this.properties = properties;
            }

            public class TargetProperties implements Serializable {
                @SerializedName("houseID")
                @Expose
                private String houseID;
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
                @SerializedName("latitude")
                @Expose
                private String latitude;
                @SerializedName("longitude")
                @Expose
                private String longitude;
                @SerializedName("totalFamilyMembers")
                @Expose
                private String totalFamilyMembers;
                @SerializedName("houseHeadName")
                @Expose
                private String houseHeadName;
                @SerializedName("hasEligibleCouple")
                @Expose
                private boolean hasEligibleCouple;
                @SerializedName("hasPregnantWoman")
                @Expose
                private boolean hasPregnantWoman;
                @SerializedName("hasNewBorn")
                @Expose
                private boolean hasNewBorn;
                @SerializedName("pregnantLadyName")
                @Expose
                private String pregnantLadyName;
                @SerializedName("houseHeadImageUrls")
                @Expose
                private List<String> houseHeadImageUrls;

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public String getTotalFamilyMembers() {
                    return totalFamilyMembers;
                }

                public void setTotalFamilyMembers(String totalFamilyMembers) {
                    this.totalFamilyMembers = totalFamilyMembers;
                }

                public String getHouseHeadName() {
                    return houseHeadName;
                }

                public void setHouseHeadName(String houseHeadName) {
                    this.houseHeadName = houseHeadName;
                }

                public boolean isHasEligibleCouple() {
                    return hasEligibleCouple;
                }

                public void setHasEligibleCouple(boolean hasEligibleCouple) {
                    this.hasEligibleCouple = hasEligibleCouple;
                }

                public boolean isHasPregnantWoman() {
                    return hasPregnantWoman;
                }

                public void setHasPregnantWoman(boolean hasPregnantWoman) {
                    this.hasPregnantWoman = hasPregnantWoman;
                }

                public boolean isHasNewBorn() {
                    return hasNewBorn;
                }

                public void setHasNewBorn(boolean hasNewBorn) {
                    this.hasNewBorn = hasNewBorn;
                }

                public String getPregnantLadyName() {
                    return pregnantLadyName;
                }

                public void setPregnantLadyName(String pregnantLadyName) {
                    this.pregnantLadyName = pregnantLadyName;
                }

                public List<String> getHouseHeadImageUrls() {
                    return houseHeadImageUrls;
                }

                public void setHouseHeadImageUrls(List<String> houseHeadImageUrls) {
                    this.houseHeadImageUrls = houseHeadImageUrls;
                }

                public String getHouseID() {
                    return houseID;
                }

                public void setHouseID(String houseID) {
                    this.houseID = houseID;
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

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

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
            private RMNCHAPNCWomenResponse.Content.Relationship.RelationshipProperties properties;

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

            public class RelationshipProperties implements Serializable {

            }
        }

        public class Source implements Serializable {

            @SerializedName("id")
            @Expose
            private long id;
            @SerializedName("labels")
            @Expose
            private List<String> labels = null;
            @SerializedName("properties")
            @Expose
            private RMNCHAPNCWomenResponse.Content.Source.SourceProperties properties;

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

            public RMNCHAPNCWomenResponse.Content.Source.SourceProperties getProperties() {
                return properties;
            }

            public void setProperties(RMNCHAPNCWomenResponse.Content.Source.SourceProperties sourceProperties) {
                this.properties = sourceProperties;
            }

            public class SourceProperties implements Serializable {

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
                @SerializedName("dateCreated")
                @Expose
                private String dateCreated;
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
                @SerializedName("latitude")
                @Expose
                private double latitude;
                @SerializedName("longitude")
                @Expose
                private double longitude;
                @SerializedName("houseHeadName")
                @Expose
                private String houseHeadName;

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
