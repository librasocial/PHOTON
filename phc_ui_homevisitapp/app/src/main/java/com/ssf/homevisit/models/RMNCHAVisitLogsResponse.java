package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RMNCHAVisitLogsResponse {

    @SerializedName("meta")
    @Expose
    private Meta meta = null;

    @SerializedName("data")
    @Expose
    private List<VisitLog> data = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<VisitLog> getData() {
        return data;
    }

    public void setData(List<VisitLog> data) {
        this.data = data;
    }

    public class Meta implements Serializable {

        @SerializedName("totalPages")
        @Expose
        private String totalPages;
        @SerializedName("totalElements")
        @Expose
        private String totalElements;
        @SerializedName("number")
        @Expose
        private String number;
        @SerializedName("size")
        @Expose
        private String size;

        public String getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(String totalPages) {
            this.totalPages = totalPages;
        }

        public String getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(String totalElements) {
            this.totalElements = totalElements;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }


    public class VisitLog implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("rchId")
        @Expose
        private String rchId;
        @SerializedName("serviceId")
        @Expose
        private String serviceId;
        @SerializedName("visitDate")
        @Expose
        private String visitDate;
        @SerializedName("isPregnancyTestTaken")
        @Expose
        private boolean isPregnancyTestTaken;
        @SerializedName("pregnancyTestResult")
        @Expose
        private String pregnancyTestResult;
        @SerializedName("isReferredToPHC")
        @Expose
        private String isReferredToPHC;
        @SerializedName("bcOcpType")
        @Expose
        private String bcOcpType;
        @SerializedName("bcQuantity")
        @Expose
        private String bcQuantity;
        @SerializedName("pcContraceptiveStopDate")
        @Expose
        private String pcContraceptiveStopDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRchId() {
            return rchId;
        }

        public void setRchId(String rchId) {
            this.rchId = rchId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(String visitDate) {
            this.visitDate = visitDate;
        }

        public boolean isPregnancyTestTaken() {
            return isPregnancyTestTaken;
        }

        public void setPregnancyTestTaken(boolean pregnancyTestTaken) {
            isPregnancyTestTaken = pregnancyTestTaken;
        }

        public String getPregnancyTestResult() {
            return pregnancyTestResult;
        }

        public void setPregnancyTestResult(String pregnancyTestResult) {
            this.pregnancyTestResult = pregnancyTestResult;
        }

        public String getIsReferredToPHC() {
            return isReferredToPHC;
        }

        public void setIsReferredToPHC(String isReferredToPHC) {
            this.isReferredToPHC = isReferredToPHC;
        }

        public String getBcOcpType() {
            return bcOcpType;
        }

        public void setBcOcpType(String bcOcpType) {
            this.bcOcpType = bcOcpType;
        }

        public String getBcQuantity() {
            return bcQuantity;
        }

        public void setBcQuantity(String bcQuantity) {
            this.bcQuantity = bcQuantity;
        }

        public String getPcContraceptiveStopDate() {
            return pcContraceptiveStopDate;
        }

        public void setPcContraceptiveStopDate(String pcContraceptiveStopDate) {
            this.pcContraceptiveStopDate = pcContraceptiveStopDate;
        }
    }


}
