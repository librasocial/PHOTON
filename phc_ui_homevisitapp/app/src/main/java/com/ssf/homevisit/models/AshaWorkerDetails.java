package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AshaWorkerDetails {
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;

    @SerializedName("content")
    @Expose
    private List<AllPhcResponse.Content> content = null;

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

    public List<AllPhcResponse.Content> getContent() {
        return content;
    }

    public void setContent(List<AllPhcResponse.Content> content) {
        this.content = content;
    }

    public class Content {

        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("contact")
        @Expose
       private int contactNo;

        @SerializedName("phc")
        @Expose
       private String phc;

        @SerializedName("phc_sub_center")
        @Expose
       private String phccenter;

        @SerializedName("worker_id")
        @Expose
       private String workid;

        @SerializedName("type")
        @Expose
       private String ashaty;

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

        public int getContactNo() {
            return contactNo;
        }

        public void setContactNo(int contactNo) {
            this.contactNo = contactNo;
        }

        public String getPhc() {
            return phc;
        }

        public void setPhc(String phc) {
            this.phc = phc;
        }

        public String getPhccenter() {
            return phccenter;
        }

        public void setPhccenter(String phccenter) {
            this.phccenter = phccenter;
        }

        public String getWorkid() {
            return workid;
        }

        public void setWorkid(String workid) {
            this.workid = workid;
        }

        public String getAshaty() {
            return ashaty;
        }

        public void setAshaty(String ashaty) {
            this.ashaty = ashaty;
        }
    }
}
