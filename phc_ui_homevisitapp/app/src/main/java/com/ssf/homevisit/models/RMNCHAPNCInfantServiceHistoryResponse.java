package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RMNCHAPNCInfantServiceHistoryResponse implements Serializable {

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
    @SerializedName("data")
    @Expose
    private List<RMNCHAPNCInfantVisitLogResponse> data = null;

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

    public List<RMNCHAPNCInfantVisitLogResponse> getData() {
        return data;
    }

    public void setData(List<RMNCHAPNCInfantVisitLogResponse> data) {
        this.data = data;
    }
}
