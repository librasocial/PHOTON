package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AshaWorkerResponse {

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("content")
    @Expose
    private List<AshaWorkerResponse.Content> content = null;

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

    public List<AshaWorkerResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<AshaWorkerResponse.Content> content) {
        this.content = content;
    }

    public class Content {
        @SerializedName("sourceNode")
        @Expose
        private SourceNode sourceNode;
        @SerializedName("targetNode")
        @Expose
        private TargetNode targetNode;
        @SerializedName("relationshipType")
        @Expose
        private String relationshipType;

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

        public String getRelationshipType() {
            return relationshipType;
        }

        public void setRelationshipType(String relationshipType) {
            this.relationshipType = relationshipType;
        }

        public class TargetNode {

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

                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("name")
                @Expose
                private String name;
                @SerializedName("contact")
                @Expose
                private String contact;
                @SerializedName("phc")
                @Expose
                private String phc;
                @SerializedName("type")
                @Expose
                private String type;

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getContact() {
                    return contact;
                }

                public void setContact(String contact) {
                    this.contact = contact;
                }

                public String getPhc() {
                    return phc;
                }

                public void setPhc(String phc) {
                    this.phc = phc;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

        }

        public class SourceNode {

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

                @SerializedName("uuid")
                @Expose
                private String uuid;
                @SerializedName("name")
                @Expose
                private String name;
                @SerializedName("latitude")
                @Expose
                private double latitude;
                @SerializedName("longitude")
                @Expose
                private double longitude;
                @SerializedName("type")
                @Expose
                private String type;

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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
