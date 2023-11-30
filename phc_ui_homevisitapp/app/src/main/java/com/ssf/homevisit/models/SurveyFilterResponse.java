package com.ssf.homevisit.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SurveyFilterResponse {


    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }





    public class Datum {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("surveyType")
        @Expose
        private String surveyType;
        @SerializedName("version")
        @Expose
        private String version;
        @SerializedName("quesOptions")
        @Expose
        private List<QuesOption> quesOptions = null;
        @SerializedName("audit")
        @Expose
        private Audit audit;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSurveyType() {
            return surveyType;
        }

        public void setSurveyType(String surveyType) {
            this.surveyType = surveyType;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public List<QuesOption> getQuesOptions() {
            return quesOptions;
        }

        public void setQuesOptions(List<QuesOption> quesOptions) {
            this.quesOptions = quesOptions;
        }

        public Audit getAudit() {
            return audit;
        }

        public void setAudit(Audit audit) {
            this.audit = audit;
        }

        public class Audit {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("createdBy")
            @Expose
            private String createdBy;
            @SerializedName("dateCreated")
            @Expose
            private String dateCreated;
            @SerializedName("modifiedBy")
            @Expose
            private String modifiedBy;
            @SerializedName("dateModified")
            @Expose
            private String dateModified;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getDateCreated() {
                return dateCreated;
            }

            public void setDateCreated(String dateCreated) {
                this.dateCreated = dateCreated;
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

        }
        public class QuesOption {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("question")
            @Expose
            private String question;
            @SerializedName("choices")
            @Expose
            private List<String> choices = null;
            @SerializedName("propertyName")
            private String propertyName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public List<String> getChoices() {
                return choices;
            }

            public void setChoices(List<String> choices) {
                this.choices = choices;
            }

            public String getPropertyName() {
                return propertyName;
            }

            public void setPropertyName(String propertyName) {
                this.propertyName = propertyName;
            }
        }

    }
    public class Meta {

        @SerializedName("totalPages")
        @Expose
        private Integer totalPages;
        @SerializedName("totalElements")
        @Expose
        private Integer totalElements;
        @SerializedName("number")
        @Expose
        private Integer number;
        @SerializedName("size")
        @Expose
        private Integer size;

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

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

    }


}