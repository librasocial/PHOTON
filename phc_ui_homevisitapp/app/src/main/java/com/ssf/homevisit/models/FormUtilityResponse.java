package com.ssf.homevisit.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FormUtilityResponse {
    @SerializedName("formItems")
    private List<SurveyFormResponse> data;

    public List<SurveyFormResponse> getData() {
        return data;
    }

    public void setData(List<SurveyFormResponse> data) {
        this.data = data;
    }

    public class SurveyFormResponse {

        @SerializedName("groupName")
        private String groupName;
        @SerializedName("elements")
        private List<Options> quesOptions;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public List<Options> getQuesOptions() {
            return quesOptions;
        }

        public void setQuesOptions(List<Options> quesOptions) {
            this.quesOptions = quesOptions;
        }
    }

    public class Options {
        @SerializedName("title")
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

