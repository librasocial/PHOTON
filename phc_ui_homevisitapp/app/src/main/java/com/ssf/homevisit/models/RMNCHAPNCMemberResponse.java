package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RMNCHAPNCMemberResponse {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<RMNCHAPNCMemberResponse.Content> content;

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

    public List<RMNCHAPNCMemberResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<RMNCHAPNCMemberResponse.Content> content) {
        this.content = content;
    }

    public class Content implements Serializable {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("labels")
        @Expose
        private List<String> labels = null;
        @SerializedName("properties")
        @Expose
        private RMNCHAPNCMemberResponse.Content.Properties properties;

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

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public class Properties implements Serializable {

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
            @SerializedName("isAdult")
            @Expose
            private boolean isAdult;
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

            public boolean isAdult() {
                return isAdult;
            }

            public void setAdult(boolean adult) {
                isAdult = adult;
            }

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
