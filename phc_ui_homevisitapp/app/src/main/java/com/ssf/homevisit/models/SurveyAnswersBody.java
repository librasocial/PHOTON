package com.ssf.homevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurveyAnswersBody {
    private String surveyId;
    private String  surveyName;
    private String surveyType;
    private SurveyAnswerContext context;
    private List<QuestionResponse> quesResponse;

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }
    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public SurveyAnswerContext getContext() {
        return context;
    }

    public void setContext(SurveyAnswerContext context) {
        this.context = context;
    }

    public List<QuestionResponse> getQuesResponse() {
        return quesResponse;
    }

    public void setQuesResponse(List<QuestionResponse> quesResponse) {
        this.quesResponse = quesResponse;
    }

}
