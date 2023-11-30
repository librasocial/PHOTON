package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesCountResponse {
    @SerializedName("totalPages")
    @Expose
    private String totalPages;
    @SerializedName("totalElements")
    @Expose
    private String totalElements;

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

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    @SerializedName("content")
    @Expose
    private List<Content> content;

    public static class Content{
        @SerializedName("assetType")
        @Expose
        private String assetType;

        public String getAssetType() {
            return assetType;
        }

        public void setAssetType(String assetType) {
            this.assetType = assetType;
        }

        public int getTypeCount() {
            return typeCount;
        }

        public void setTypeCount(int typeCount) {
            this.typeCount = typeCount;
        }

        @SerializedName("typeCount")
        @Expose
        private int typeCount;
    }
}
